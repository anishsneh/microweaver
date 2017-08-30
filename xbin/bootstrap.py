import os
import yaml
import json
from kubernetes import client, config
from kubernetes.client.rest import ApiException
from operator import itemgetter
import datetime
import glob
import time

_MICROWEAVER_HOME = os.getenv("MICROWEAVER_HOME")

_master_conf_path_tpl = "{}/xconf/master.yaml"

_sync_deployment = True
_min_replicas = 1
_max_attempts = 20
_attempt_wait = 5

_SYSTEM_REGISTRY_TPL = "http://{0}:{1}/eureka/"
_SERVICE_FQDN_TPL = "{0}.{1}.svc.{2}";

def log(message):
    print("[{ts}] {msg}".format(ts = str(datetime.datetime.now()), msg = message))

def _pretty_print_yaml(yaml_object, title = None):
    log(yaml.safe_dump(yaml_object))

def _pretty_print_json(json_object, title = None):
    if not title is None:
        log("---------------------------------------- [{t}] ----------------------------------------".format(t = title)) 
    log(json.dumps(json_object, sort_keys = True, indent = 4))

def _get_yaml_object(yaml_path):
    with open(yaml_path) as yaml_file:
        configuration = yaml.load(yaml_file)
    return configuration

def _get_json_object(json_path):
    with open(json_path) as json_file:
        configuration = json.load(json_file)
    return configuration

def _flatten_dict(d, result=None):
    if result is None:
        result = {}
    for key in d:
        value = d[key]
        if isinstance(value, dict):
            value1 = {}
            for keyIn in value:
                value1[".".join([key,keyIn])]=value[keyIn]
            _flatten_dict(value1, result)
        elif isinstance(value, (list, tuple)):   
            for indexB, element in enumerate(value):
                if isinstance(element, dict):
                    value1 = {}
                    index = 0
                    for keyIn in element:
                        newkey = ".".join([key,keyIn])        
                        value1[".".join([key,keyIn])]=value[indexB][keyIn]
                        index += 1
                    for keyA in value1:
                        _flatten_dict(value1, result)   
        else:
            result[key]=str(value)
    return result

def _sample_api_call_within_pod():
    token = None
    with open('/var/run/secrets/kubernetes.io/serviceaccount/token', 'r') as token_file:
        token = token_file.read()    
    client.configuration.api_key_prefix['authorization'] = 'Bearer'
    client.configuration.api_key['authorization'] = token
    client.configuration.host="https://kubernetes.default"
    client.configuration.verify_ssl=False
    v1 = client.CoreV1Api()
    log(str(v1.list_node()))
    
    with open('/apache.yaml', 'r') as deployment_file:
        dep = yaml.load(deployment_file)
        k8s_beta = client.ExtensionsV1beta1Api()
        resp = k8s_beta.create_namespaced_deployment(body=dep, namespace="microweaver-system")
        log("Deployment created. status='%s'" % str(resp.status))

def _get_client(system_conf, api_version = None):
    api_host = _get_configuration(system_conf, "CONTAINER", "KUBERNETES", "API_SERVER", "host")
    api_port = _get_configuration(system_conf, "CONTAINER", "KUBERNETES", "API_SERVER", "port")
    api_scheme = _get_configuration(system_conf, "CONTAINER", "KUBERNETES", "API_SERVER", "scheme")
    api_url = "{s}://{h}:{p}".format(s = api_scheme, h = api_host, p = api_port)
    client.configuration.api_key_prefix['authorization'] = 'Bearer'
    client.configuration.api_key['authorization'] = ''
    client.configuration.host=api_url
    client.configuration.verify_ssl=False
    if api_version == "V1":
        client_obj = client.CoreV1Api()
    elif api_version == "EXTENSIONS_V1BETA1":
        client_obj = client.ExtensionsV1beta1Api()
    else:
        raise SystemExit("Invalid API version [{}]".format(api_version))
    return client_obj

def _check_deployment_status(master_conf, deployment):
    attempts = 0
    success = False
    system_conf = master_conf.get("master").get("configurations").get("system").values()
    system_namespace = master_conf.get("master").get("namespace").get("system")
    client = _get_client(system_conf, "EXTENSIONS_V1BETA1")
    while (not success) and (attempts < _max_attempts):
        try:
            deployment_status = client.read_namespaced_deployment_status(deployment.get("name"), system_namespace, pretty = "true")
            status = deployment_status.status
            log("=================================== DEPLOYMENT STATUS [{}] ===================================".format(deployment.get("name")))
            log(str(status))
            if(status.available_replicas >= _min_replicas):
                log("++++++++++++++++++++++++++++++ Got desired number of replicas up and running for [{}]".format(deployment.get("name")))
                success = True
        except Exception as e:
            log("Deployment is not ready yet, will retry [{}]".format(str(e)))
            attempts = attempts + 1
        time.sleep(_attempt_wait)
    if not success:
        raise SystemExit("ERROR: Failed to find minimum number of replicas [{r}] for service [{s}], exiting with error".format(r = _min_replicas, s = deployment.get("name")))

# TODO Change to use Jinja2 templates
def _create_namespace(master_conf, deployment, ro):
    system_conf = master_conf.get("master").get("configurations").get("system").values()
    system_namespace = master_conf.get("master").get("namespace").get("system")
    ro["metadata"]["name"] = system_namespace
    _pretty_print_json(ro, "Namespace definition")
    client = _get_client(system_conf, "V1")
    try:
        response = client.create_namespace(ro, pretty = 'true')
        log("Namespace creation triggered, response ['{}']".format(str(response)))
    except ApiException as e:
        raise SystemExit("ERROR: Failed to create namespace [{}]".format(e))

# TODO Change to use Jinja2 templates
def _create_configmap(master_conf, deployment, ro):
    system_conf = master_conf.get("master").get("configurations").get("system").values()
    system_namespace = master_conf.get("master").get("namespace").get("system")
    if "metadata" in ro:
        md = ro["metadata"]
        md["name"] = deployment.get("name")
        md["namespace"] = system_namespace
    if "data" in ro:
         ro["data"] = _flatten_dict(master_conf)
    _pretty_print_json(ro, "Configuration map definition")
    client = _get_client(system_conf, "V1")
    try:
        response = client.create_namespaced_config_map(system_namespace, ro, pretty = 'true')
        log("Configuration map creation triggered, response ['{}']".format(str(response)))
    except ApiException as e:
        raise SystemExit("ERROR: Failed to create configuration map [{}]".format(e))

# TODO Change to use Jinja2 templates
def _create_deployment(master_conf, deployment, ro):
    system_conf = master_conf.get("master").get("configurations").get("system").values()
    registry_host = _get_configuration(system_conf, "CONTAINER", "DOCKER_ENGINE", "REGISTRY", "host")
    registry_port = _get_configuration(system_conf, "CONTAINER", "DOCKER_ENGINE", "REGISTRY", "port")
    port_index = 0
    container_index = 0
    system_namespace = master_conf.get("master").get("namespace").get("system")
    system_domain = master_conf.get("master").get("domain")
    if "metadata" in ro:
        md = ro["metadata"]
        md["name"] = deployment.get("name")
        md["namespace"] = system_namespace
        if "labels" in md:
            md["labels"]["app"] = deployment.get("name")
    if "spec" in ro:
        sp = ro["spec"]
        sp["replicas"] = deployment.get("replicas")
        if "selector" in sp:
            sl = sp["selector"]
            if "matchLabels" in sl:
                sl["matchLabels"]["app"] = deployment.get("name")
        if "template" in sp:
            tpl = sp["template"]
            if "metadata" in tpl:
                tplmd = tpl["metadata"]
                if "labels" in tplmd:
                    tplmd["labels"]["app"] = deployment.get("name")
                tplmd["name"] = deployment.get("name")
                tplmd["namespace"] = system_namespace
            
            if "spec" in tpl:
                tplspec = tpl["spec"]
                tplspec["containers"][container_index]["name"] = deployment.get("name")
                image = "{h}:{p}/{n}:{t}".format(h = registry_host, p = registry_port, n = deployment.get("image").get("name"), t = deployment.get("image").get("tag"))
                if "containers" in tplspec:
                    tplspec["containers"][container_index]["image"] = image
                    
                    if deployment.get("limits"):
                        if deployment.get("cpu"):
                            if deployment.get("cpu").strip() != "UNLIMITED":
                                tplspec["containers"][container_index]["resources"]["limits"]["cpu"] = deployment.get("cpu")
                        if deployment.get("memory"):
                            if deployment.get("memory").strip() != "UNLIMITED":
                                tplspec["containers"][container_index]["resources"]["limits"]["memory"] = deployment.get("memory")
                    
                    deployments_defs = master_conf.get("master").get("deployments")
                    registry_01 = deployments_defs.get("REGISTRY_SERVICE_01")
                    registry_02 = deployments_defs.get("REGISTRY_SERVICE_02")
                    db_service = deployments_defs.get("DATABASE_SERVICE")
                    mq_service = deployments_defs.get("MESSAGING_SERVICE")
                    
                    system_registry_default_zone = []
                    if registry_01.get("enabled"):
                        registry_01_fqdn = _SERVICE_FQDN_TPL.format(registry_01.get("name"), system_namespace, system_domain)
                        system_registry_01_url = _SYSTEM_REGISTRY_TPL.format(registry_01_fqdn, registry_01.get("port"))
                        system_registry_default_zone.append(system_registry_01_url)
                    if registry_02.get("enabled"):
                        registry_02_fqdn = _SERVICE_FQDN_TPL.format(registry_02.get("name"), system_namespace, system_domain)
                        system_registry_02_url = _SYSTEM_REGISTRY_TPL.format(registry_02_fqdn, registry_02.get("port"))
                        system_registry_default_zone.append(system_registry_02_url)                    
                    
                    system_registry_default_zone_str = ",".join(system_registry_default_zone)
                    environment_vars = [
                        {
                            "name" : "MICROSERVICE_SERVICE_NAME",
                            "value" : _SERVICE_FQDN_TPL.format(deployment.get("name"), system_namespace, system_domain)
                        },
                        {
                            "name" : "SYSTEM_REGISTRY_DEFAULT_ZONE",
                            "value" : system_registry_default_zone_str
                        },
                        {
                            "name" : "SYSTEM_DB_HOST",
                            "value" : _SERVICE_FQDN_TPL.format(db_service.get("name"), system_namespace, system_domain)
                        },
                        {
                            "name" : "SYSTEM_DB_SECRET",
                            "value" : db_service.get("secret")
                        },
                        {
                            "name" : "SYSTEM_MQ_HOST",
                            "value" : _SERVICE_FQDN_TPL.format(mq_service.get("name"), system_namespace, system_domain)
                        },
                        {
                            "name" : "SYSTEM_MQ_SECRET",
                            "value" : mq_service.get("secret")
                        },
                        {    "name" : "SYSTEM_DOMAIN",
                            "value" : system_domain
                        }
                    ]
                    tplspec["containers"][container_index]["env"] = environment_vars
    _pretty_print_json(ro, "Deployment definition")
    client = _get_client(system_conf, "EXTENSIONS_V1BETA1")
    try:
        response = client.create_namespaced_deployment(system_namespace, ro, pretty = 'true')
        log("Service creation triggered, response ['{}']".format(str(response)))
    except ApiException as e:
        raise SystemExit("ERROR: Failed to create deployment [{}]".format(e))
    
# TODO Change to use Jinja2 templates
def _create_service(master_conf, deployment, ro):
    system_conf = master_conf.get("master").get("configurations").get("system").values()
    port_index = 0
    container_index = 0
    system_namespace = master_conf.get("master").get("namespace").get("system")
    if "metadata" in ro:
        md = ro["metadata"]
        md["name"] = deployment.get("name")
        md["namespace"] = system_namespace
        if "labels" in md:
            md["labels"]["app"] = deployment.get("name")
    if "spec" in ro:
        sp = ro["spec"]
        if "selector" in sp:
            sp["selector"]["app"] = deployment.get("name")
    _pretty_print_json(ro, "Service definition")
    client = _get_client(system_conf, "V1")
    try:
        response = client.create_namespaced_service(system_namespace, ro, pretty = 'true')
        log("Service creation triggered, response ['{}']".format(str(response)))
    except ApiException as e:
        raise SystemExit("ERROR: Failed to create service [{}]".format(e))

# TODO Change to use Jinja2 templates
def _create_endpoints(master_conf, deployment, resource_object):
    system_conf = master_conf.get("master").get("configurations").get("system").values()
    subset_index = 0
    port_index = 0
    address_index = 0
    system_namespace = master_conf.get("master").get("namespace").get("system")
    resource_object["metadata"]["name"] = deployment.get("name")
    resource_object["metadata"]["namespace"] = system_namespace
    resource_object["subsets"][subset_index]["addresses"][address_index]["ip"] = deployment.get("externalHost")
    resource_object["subsets"][subset_index]["ports"][port_index]["port"] = deployment.get("port")
    _pretty_print_json(resource_object, "Endpoint definition")
    client = _get_client(system_conf, "V1")
    try:
        response = client.create_namespaced_endpoints(system_namespace, resource_object, pretty = 'true')
        log("Endpoints creation triggered, response ['{}']".format(str(response)))
    except ApiException as e:
        raise SystemExit("ERROR: Failed to create endpoints [{}]".format(e))

def _create_deployments(master_conf):
    system_conf = master_conf.get("master").get("configurations").get("system").values()
    deployments = master_conf.get("master").get("deployments").values()
    deployments_sorted_by_index = sorted(deployments, key = itemgetter('index'), reverse = False)
    for deployment in deployments_sorted_by_index:
        if(not deployment.get("enabled")):
            log("============================ SKIPPING DEPLOYMENT [{}] ============================".format(deployment.get("name")))
            continue
        log("============================ PROCESSING DEPLOYMENT [{}] ============================".format(deployment.get("name")))
        resources_dir = "{mwh}/{ct}/{dir}/blueprints".format(mwh = _MICROWEAVER_HOME, ct = deployment.get("category"), dir = deployment.get("directory"))
        os.chdir(resources_dir)
        resources_files = glob.glob("{key}-resource-*.yaml".format(key = deployment.get("key")))
        resources_files_sorted = sorted(resources_files)
        for file in resources_files_sorted:
            resource_file = "{dir}/{rf}".format(dir = resources_dir, rf = file)
            log("Processing resource [{rf}]".format(rf = resource_file))
            path_exists = os.path.isfile(resource_file)
            if not path_exists:
                raise SystemExit("File [{}] does not exist".format(resource_file))
            else:
                log("File [{}] exists".format(resource_file))
            resource_object = _get_yaml_object(resource_file)
            if resource_object.get("kind") == "Namespace":
                log("Creating namespace")
                _create_namespace(master_conf, deployment, resource_object)
            elif resource_object.get("kind") == "Service":
                log("Creating service")
                _create_service(master_conf, deployment, resource_object)
            elif resource_object.get("kind") == "Endpoints":
                log("Creating endpoints")
                _create_endpoints(master_conf, deployment, resource_object)
            elif resource_object.get("kind") == "Deployment":
                log("Creating deployment")
                _create_deployment(master_conf, deployment, resource_object)   
            elif resource_object.get("kind") == "ConfigMap":
                log("Creating config map")
                _create_configmap(master_conf, deployment, resource_object)
            else:
                raise SystemExit("ERROR: Unsupported resource kind [{}]".format(resource_object.get("kind")))
        if "microweaver-services" == deployment.get("category") and _sync_deployment:
            _check_deployment_status(master_conf, deployment)
        
def _validate_configuration(master_conf):
    log("Validating master configuration")
    log("Checking connectivity")

def _get_configuration(configurations, category, provider, component, key):
    tc = [ configuration for configuration in configurations if category == configuration.get("category") and provider == configuration.get("provider") and component == configuration.get("component") ]
    return tc[0].get(key)
    
def main():
    if _MICROWEAVER_HOME is None:
        raise SystemExit("MICROWEAVER_HOME is not defined, exiting")
    master_conf = _get_yaml_object(_master_conf_path_tpl.format(_MICROWEAVER_HOME))
    _validate_configuration(master_conf)
    _pretty_print_json(master_conf, "MASTER CONFIGURATION")
    
    system_namespace = master_conf.get("master").get("namespace").get("system")
    log("System namespace [{}]".format(system_namespace))
    
    system_conf = master_conf.get("master").get("configurations").get("system").values()
    _pretty_print_json(system_conf, "SYSTEM CONFIGURATION")
    
    _create_deployments(master_conf)
    
if __name__ == "__main__":
    main()

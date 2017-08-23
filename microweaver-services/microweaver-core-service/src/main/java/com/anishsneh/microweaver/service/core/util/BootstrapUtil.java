package com.anishsneh.microweaver.service.core.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.anishsneh.microweaver.service.core.vo.Service;

/**
 * The Class BootstrapUtil.
 * 
 * @author Anish Sneh
 * 
 */
public class BootstrapUtil {
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(BootstrapUtil.class);

	/** The Constant PATTERN_DEPLOYMENT_NAME. */
	// master.bootstrap.deployments.BOOTSTRAP_GATEWAY_SERVICE.id
	private static final String PATTERN_DEPLOYMENT_NAME = "master\\.deployments\\.([0-9a-zA-Z_]+)\\.name";
	
	/** The Constant MASTER_DEPLOYMENTS_IMAGE_NAME_KEY. */
	// Bootstrap deployment properties
	private static final String MASTER_DEPLOYMENTS_IMAGE_NAME_KEY = "master.deployments.%s.image.name";
	
	/** The Constant MASTER_DEPLOYMENTS_IMAGE_PREFIX_KEY. */
	private static final String MASTER_DEPLOYMENTS_IMAGE_PREFIX_KEY = "master.deployments.%s.image.prefix";
	
	/** The Constant MASTER_DEPLOYMENTS_IMAGE_TAG_KEY. */
	private static final String MASTER_DEPLOYMENTS_IMAGE_TAG_KEY = "master.deployments.%s.image.tag";
	
	/** The Constant MASTER_DEPLOYMENTS_NAME_KEY. */
	private static final String MASTER_DEPLOYMENTS_NAME_KEY = "master.deployments.%s.name";
	
	/** The Constant MASTER_DEPLOYMENTS_PORT_KEY. */
	private static final String MASTER_DEPLOYMENTS_PORT_KEY = "master.deployments.%s.port";
	
	/** The Constant MASTER_DEPLOYMENTS_REPLICAS_KEY. */
	private static final String MASTER_DEPLOYMENTS_REPLICAS_KEY = "master.deployments.%s.replicas";
	
	/** The Constant MASTER_DEPLOYMENTS_INDEX_KEY. */
	private static final String MASTER_DEPLOYMENTS_INDEX_KEY = "master.deployments.%s.index";
	
	/** The Constant MASTER_DEPLOYMENTS_TYPE_KEY. */
	private static final String MASTER_DEPLOYMENTS_TYPE_KEY = "master.deployments.%s.type";
	
	/** The Constant MASTER_DEPLOYMENTS_SIDECAR_PORT_KEY. */
	private static final String MASTER_DEPLOYMENTS_SIDECAR_PORT_KEY = "master.deployments.%s.sidecarPort";
	
	/** The Constant MASTER_NAMESPACE_SYSTEM_KEY. */
	// Fixed master properties
	private static final String MASTER_NAMESPACE_SYSTEM_KEY = "master.namespace.system";
	
	/** The Constant MASTER_IMAGE_REGISTRY_HOST_KEY. */
	private static final String MASTER_IMAGE_REGISTRY_HOST_KEY = "master.configurations.system.DOCKER_ENGINE_REGISTRY.host";
	
	/** The Constant MASTER_IMAGE_REGISTRY_PORT_KEY. */
	private static final String MASTER_IMAGE_REGISTRY_PORT_KEY = "master.configurations.system.DOCKER_ENGINE_REGISTRY.port";
	
	/** The Constant MASTER_REGISTRY_SERVICE_01_NAME_KEY. */
	private static final String MASTER_REGISTRY_SERVICE_01_NAME_KEY = "master.deployments.REGISTRY_SERVICE_01.name";
	
	/** The Constant MASTER_REGISTRY_SERVICE_02_NAME_KEY. */
	private static final String MASTER_REGISTRY_SERVICE_02_NAME_KEY = "master.deployments.REGISTRY_SERVICE_02.name";
	
	/** The Constant MASTER_SYSTEM_DOMAIN_KEY. */
	private static final String MASTER_SYSTEM_DOMAIN_KEY = "master.domain";
	
	/** The Constant SERVICE_FQDN_TPL. */
	private static final String SERVICE_FQDN_TPL = "%s.%s.svc.%s";
	
	/** The Constant MASTER_DB_SERVICE_NAME_KEY. */
	private static final String MASTER_DB_SERVICE_NAME_KEY = "master.deployments.DATABASE_SERVICE.name";
	
	/** The Constant MASTER_DB_SERVICE_PORT_KEY. */
	private static final String MASTER_DB_SERVICE_PORT_KEY = "master.deployments.DATABASE_SERVICE.port";
	
	/** The Constant MASTER_DB_SERVICE_SECRET_KEY. */
	private static final String MASTER_DB_SERVICE_SECRET_KEY = "master.deployments.DATABASE_SERVICE.secret";
	
	/** The Constant MASTER_MQ_SERVICE_NAME_KEY. */
	private static final String MASTER_MQ_SERVICE_NAME_KEY = "master.deployments.MESSAGING_SERVICE.name";
	
	/** The Constant MASTER_MQ_SERVICE_PORT_KEY. */
	private static final String MASTER_MQ_SERVICE_PORT_KEY = "master.deployments.MESSAGING_SERVICE.port";
	
	/**
	 * Gets the deployment key.
	 *
	 * @param deploymentNameKey the deployment name key
	 * @return the deployment key
	 */
	private static String getDeploymentKey(final String deploymentNameKey) {
		Pattern p = Pattern.compile(PATTERN_DEPLOYMENT_NAME);
		Matcher m = p.matcher(deploymentNameKey);
		String deploymentName = null;
		while (m.find()) {
			deploymentName = m.group(1);
		}
		return deploymentName;
	}
	
	/**
	 * Gets the deployment namespace.
	 *
	 * @param configData the config data
	 * @return the deployment namespace
	 */
	public static String getSystemNamespace(final Map<String, String> configData) {
		return configData.get(MASTER_NAMESPACE_SYSTEM_KEY);
	}
	
	/**
	 * Gets the system domain.
	 *
	 * @param configData the config data
	 * @return the system domain
	 */
	public static String getSystemDomain(final Map<String, String> configData) {
		return configData.get(MASTER_SYSTEM_DOMAIN_KEY);
	}
	
	/**
	 * Gets the fully qualified service name.
	 *
	 * @param service the service
	 * @param configData the config data
	 * @param serviceName the service name
	 * @return the fully qualified service name
	 */
	public static String getFullyQualifiedServiceName(final Service service, final Map<String, String> configData, final String serviceName) {
		final String systemNamespace = getSystemNamespace(configData);
		final String serviceNamespace = service.getNamespace();
		final String systemDomain = getSystemDomain(configData);
		String serviceHostname = null;
		if(systemNamespace.equals(serviceNamespace)) {
			return serviceName;
		}
		else {
			serviceHostname = String.format(SERVICE_FQDN_TPL, serviceName, systemNamespace, systemDomain);
		}
		logger.info("Got fully qualified service hostname [{}] for service namespace [{}], system namespace [{}]", serviceHostname, serviceNamespace, systemNamespace);
		return serviceHostname;
	}
	
	/**
	 * Gets the system db host.
	 *
	 * @param service the service
	 * @param configData the config data
	 * @return the system db host
	 */
	public static String getSystemDbHost(final Service service, final Map<String, String> configData) {
		return getFullyQualifiedServiceName(service, configData, configData.get(MASTER_DB_SERVICE_NAME_KEY));
	}
	
	/**
	 * Gets the system db port.
	 *
	 * @param configData the config data
	 * @return the system db port
	 */
	public static Integer getSystemDbPort(final Map<String, String> configData) {
		return (null != configData.get(MASTER_DB_SERVICE_PORT_KEY)) ? Integer.parseInt(configData.get(MASTER_DB_SERVICE_PORT_KEY)) : null;
	}
	
	/**
	 * Gets the system mq host.
	 *
	 * @param service the service
	 * @param configData the config data
	 * @return the system mq host
	 */
	public static String getSystemMqHost(final Service service, final Map<String, String> configData) {
		return getFullyQualifiedServiceName(service, configData, configData.get(MASTER_MQ_SERVICE_NAME_KEY));
	}
	
	/**
	 * Gets the system mq port.
	 *
	 * @param configData the config data
	 * @return the system mq port
	 */
	public static Integer getSystemMqPort(final Map<String, String> configData) {
		return (null != configData.get(MASTER_MQ_SERVICE_PORT_KEY)) ? Integer.parseInt(configData.get(MASTER_MQ_SERVICE_PORT_KEY)) : null;
	}
	
	/**
	 * Gets the deployment sidecar port.
	 *
	 * @param configData the config data
	 * @param deploymentId the deployment id
	 * @return the deployment sidecar port
	 */
	public static Integer getDeploymentSidecarPort(final Map<String, String> configData, final String deploymentId) {
		final String key = String.format(MASTER_DEPLOYMENTS_SIDECAR_PORT_KEY, deploymentId);
		return (null != configData.get(key)) ? Integer.parseInt(configData.get(key)) : null;
	}
	
	/**
	 * Gets the image registry url.
	 *
	 * @param configData the config data
	 * @return the image registry url
	 */
	public static String getSystemImageRegistryUrl(final Map<String, String> configData) {
		final String host = configData.get(MASTER_IMAGE_REGISTRY_HOST_KEY);
		final String port = configData.get(MASTER_IMAGE_REGISTRY_PORT_KEY);
		return String.format("%s:%s", host, port);
	}
	
	/**
	 * Gets the service hostname.
	 *
	 * @param service the service
	 * @param configData the config data
	 * @return the service hostname
	 */
	public static String getServiceHostname(final Service service, final Map<String, String> configData) {
		return getFullyQualifiedServiceName(service, configData, service.getName());
	}
	
	/**
	 * Gets the registry service 01 name.
	 *
	 * @param service the service
	 * @param configData the config data
	 * @return the registry service 01 name
	 */
	public static String getRegistryService01Name(final Service service, final Map<String, String> configData) {
		return getFullyQualifiedServiceName(service, configData, configData.get(MASTER_REGISTRY_SERVICE_01_NAME_KEY));
	}
	
	/**
	 * Gets the registry service 02 name.
	 *
	 * @param service the service
	 * @param configData the config data
	 * @return the registry service 02 name
	 */
	public static String getRegistryService02Name(final Service service, final Map<String, String> configData) {
		return getFullyQualifiedServiceName(service, configData, configData.get(MASTER_REGISTRY_SERVICE_02_NAME_KEY));
	}
	
	/**
	 * Gets the deployment image name.
	 *
	 * @param configData the config data
	 * @param deploymentId the deployment id
	 * @return the deployment image name
	 */
	public static String getDeploymentImageName(final Map<String, String> configData, final String deploymentId) {
		final String key = String.format(MASTER_DEPLOYMENTS_IMAGE_NAME_KEY, deploymentId);
		return configData.get(key);
	}
	
	/**
	 * Gets the deployment image prefix.
	 *
	 * @param configData the config data
	 * @param deploymentId the deployment id
	 * @return the deployment image prefix
	 */
	public static String getDeploymentImagePrefix(final Map<String, String> configData, final String deploymentId) {
		final String key = String.format(MASTER_DEPLOYMENTS_IMAGE_PREFIX_KEY, deploymentId);
		return configData.get(key);
	}
	
	/**
	 * Gets the deployment image tag.
	 *
	 * @param configData the config data
	 * @param deploymentId the deployment id
	 * @return the deployment image tag
	 */
	public static String getDeploymentImageTag(final Map<String, String> configData, final String deploymentId) {
		final String key = String.format(MASTER_DEPLOYMENTS_IMAGE_TAG_KEY, deploymentId);
		return configData.get(key);
	}
	
	/**
	 * Gets the deployment name.
	 *
	 * @param configData the config data
	 * @param deploymentId the deployment id
	 * @return the deployment name
	 */
	public static String getDeploymentName(final Map<String, String> configData, final String deploymentId) {
		final String key = String.format(MASTER_DEPLOYMENTS_NAME_KEY, deploymentId);
		return configData.get(key);
	}
	
	/**
	 * Gets the deployment port.
	 *
	 * @param configData the config data
	 * @param deploymentId the deployment id
	 * @return the deployment port
	 */
	public static Integer getDeploymentPort(final Map<String, String> configData, final String deploymentId) {
		final String key = String.format(MASTER_DEPLOYMENTS_PORT_KEY, deploymentId);
		return (null != configData.get(key)) ? Integer.parseInt(configData.get(key)) : null;
	}
	
	/**
	 * Gets the deployment replicas.
	 *
	 * @param configData the config data
	 * @param deploymentId the deployment id
	 * @return the deployment replicas
	 */
	public static Integer getDeploymentReplicas(final Map<String, String> configData, final String deploymentId) {
		final String key = String.format(MASTER_DEPLOYMENTS_REPLICAS_KEY, deploymentId);
		return (null != configData.get(key)) ? Integer.parseInt(configData.get(key)) : null;
	}
	
	/**
	 * Gets the deployment index.
	 *
	 * @param configData the config data
	 * @param deploymentId the deployment id
	 * @return the deployment index
	 */
	public static Integer getDeploymentIndex(final Map<String, String> configData, final String deploymentId) {
		final String key = String.format(MASTER_DEPLOYMENTS_INDEX_KEY, deploymentId);
		return (null != configData.get(key)) ? Integer.parseInt(configData.get(key)) : null;
	}
	
	/**
	 * Gets the deployment type.
	 *
	 * @param configData the config data
	 * @param deploymentId the deployment id
	 * @return the deployment type
	 */
	public static String getDeploymentType(final Map<String, String> configData, final String deploymentId) {
		final String key = String.format(MASTER_DEPLOYMENTS_TYPE_KEY, deploymentId);
		return configData.get(key);
	}

	/**
	 * Gets the bootstrap deployments.
	 *
	 * @param configData the config data
	 * @return the bootstrap deployments
	 */
	public static List<String> getBootstrapDeployments(final Map<String, String> configData) {
		if(CollectionUtils.isEmpty(configData)) {
			logger.warn("Found empty configData, seems something went wrong");
			return Collections.emptyList();
		}
		logger.info("Found total [{}] config in configData map", configData.size());
		final List<String> deploymentIdList = configData.keySet().stream().filter(e -> null != getDeploymentKey(e)).map(e -> getDeploymentKey(e)).collect(Collectors.toList());
		return deploymentIdList;
	}
	
	/**
	 * Gets the filtered config data.
	 *
	 * @param configData the config data
	 * @param key the key
	 * @return the filtered config data
	 */
	public static Map<String, String> getFilteredConfigData(Map<String, String> configData, final String key){
		if(null == key) {
			return configData;
		}
		return null;
	}

	/**
	 * Gets the system db secret.
	 *
	 * @param service the service
	 * @param configData the config data
	 * @return the system db secret
	 */
	public static String getSystemDbSecret(final Service service, final Map<String, String> configData) {
		final String systemNamespace = getSystemNamespace(configData);
		final String serviceNamespace = service.getNamespace();
		if(systemNamespace.equals(serviceNamespace)) {
			return configData.get(MASTER_DB_SERVICE_SECRET_KEY);
		}
		return null;
	}
}

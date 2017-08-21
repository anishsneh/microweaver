package com.anishsneh.microweaver.service.core.infra.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anishsneh.microweaver.service.core.exception.ContainerManagerException;
import com.anishsneh.microweaver.service.core.helper.KubernetesApiHelper;
import com.anishsneh.microweaver.service.core.infra.ContainerManager;
import com.anishsneh.microweaver.service.core.util.ApiContants;
import com.anishsneh.microweaver.service.core.util.BootstrapUtil;
import com.anishsneh.microweaver.service.core.util.CommonUtil;
import com.anishsneh.microweaver.service.core.vo.Service;
import com.google.common.collect.ImmutableMap;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerPortBuilder;
import io.fabric8.kubernetes.api.model.HTTPGetActionBuilder;
import io.fabric8.kubernetes.api.model.IntOrString;
import io.fabric8.kubernetes.api.model.LabelSelector;
import io.fabric8.kubernetes.api.model.LabelSelectorRequirement;
import io.fabric8.kubernetes.api.model.Probe;
import io.fabric8.kubernetes.api.model.ProbeBuilder;
import io.fabric8.kubernetes.api.model.ServiceBuilder;
import io.fabric8.kubernetes.api.model.ServicePort;
import io.fabric8.kubernetes.api.model.ServicePortBuilder;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.DeploymentBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * The Class KubernetesContainerManagerImpl.
 * 
 * @author Anish Sneh
 * 
 */
@Component
public class KubernetesContainerManagerImpl implements ContainerManager {
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(KubernetesContainerManagerImpl.class);
	
	/** The kubernetes api helper. */
	@Autowired
	private KubernetesApiHelper kubernetesApiHelper;
	
	/**
	 * Creates the kubernetes deployment.
	 *
	 * @param service the service
	 * @param kClient the k client
	 */
	private void createKubernetesDeployment(final Service service, final KubernetesClient kClient){
		final ConfigMap masterConfigMap = kClient.configMaps().inNamespace(ApiContants.SYSTEM_NAMESPACE).withName(ApiContants.SYSTEM_CONFIGMAP_MASTER).get();
		Map<String, String> configData = BootstrapUtil.getFilteredConfigData(masterConfigMap.getData(), null);
		int replicas = service.getReplicas();
		if(null == service.isActive()) {
			// If no explicit ACTIVE flag is passed, MUST consider number of replicas parameter
			replicas = service.getReplicas();
		}
		else {
			if(service.isActive()) {
				// If explicit ACTIVE=true is passed, MUST consider number of replicas parameter
				replicas = service.getReplicas();
			}
			else {
				// If explicit ACTIVE=false is passed, MUST set number of replicas to zero
				replicas = 0;
			}
		}
		final Deployment kubeDeployment = new DeploymentBuilder()
		          .withNewMetadata()
			          .addToLabels("app", service.getName())
			          .withName(service.getName())
			          .withNamespace(service.getNamespace())
		          .endMetadata()
		          .withNewSpec()
			          .withReplicas(replicas)
			          .withSelector(new LabelSelector(new ArrayList<LabelSelectorRequirement>(), ImmutableMap.of("app", service.getName())))
			          .withNewTemplate()
				          .withNewMetadata()
					          .addToLabels("app", service.getName())
					          .withName(service.getName())
					          .withNamespace(service.getNamespace())
				          .endMetadata()
				          .withNewSpec()
				          	  .addNewContainer()
						          .withName(service.getName())
						          .withImage(String.format("%s/%s:%s", service.getRegistryUrl(), service.getImageName(), service.getImageTag()))
						          .addNewPort()
						          	.withContainerPort(service.getServicePort()).withName("http-main")
						          .endPort()
						          .withImagePullPolicy("Always")
						          .addNewEnv()
						          	.withName("SYSTEM_DB_HOST").withValue(BootstrapUtil.getSystemDbHost(service, configData))
						          .endEnv()
						          .addNewEnv()
						          	.withName("SYSTEM_MQ_HOST").withValue(BootstrapUtil.getSystemMqHost(service, configData))
						          .endEnv()
						          .addNewEnv()
						          	.withName("MICROSERVICE_SERVICE_NAME").withValue(BootstrapUtil.getServiceHostname(service, configData))
						          .endEnv()
						          .addNewEnv()
						          	.withName("EUREKA_SERVICE_NAME_01").withValue(BootstrapUtil.getRegistryService01Name(service, configData))
						          .endEnv()
						          .addNewEnv()
						          	.withName("EUREKA_SERVICE_NAME_02").withValue(BootstrapUtil.getRegistryService02Name(service, configData))
						          .endEnv()
					          .endContainer()
				          .endSpec()
				      .endTemplate()
		          .endSpec()
		          .build();		
		if(null != service.getSidecarPort()) {
			final Container container = kubeDeployment.getSpec().getTemplate().getSpec().getContainers().get(0);
			container.getPorts().add(new ContainerPortBuilder().withContainerPort(service.getSidecarPort()).withName("http-sidecar").build());
			final Probe livenessProbe = new ProbeBuilder()
					.withHttpGet(new HTTPGetActionBuilder().withPath("/health").withPort(new IntOrString("http-main")).build())
					.withInitialDelaySeconds(20)
					.build();
			final Probe readinessProbe = new ProbeBuilder()
					.withHttpGet(new HTTPGetActionBuilder().withPath("/health").withPort(new IntOrString("http-main")).build())
					.withInitialDelaySeconds(20)
					.build();
			container.setLivenessProbe(livenessProbe);
			container.setReadinessProbe(readinessProbe);
		}
		logger.info("Created kube deployment {}", CommonUtil.asJsonString(kubeDeployment));
		final Deployment kubeDeploymentResult = kClient.extensions().deployments().inNamespace(service.getNamespace()).createOrReplace(kubeDeployment);
        logger.info("Created kube deployment with status: {}", CommonUtil.asJsonString(kubeDeploymentResult.getStatus()));
	}
	
	/**
	 * Creates the kubernetes service.
	 *
	 * @param service the service
	 * @param kClient the k client
	 */
	private void createKubernetesService(final Service service, final KubernetesClient kClient){
		final io.fabric8.kubernetes.api.model.Service kubeService = new ServiceBuilder()
				.withNewMetadata()
			    		.addToLabels("app", service.getName())
			        .withName(service.getName())
			        .withNamespace(service.getNamespace())
		        .endMetadata()
		        .withNewSpec().withSelector(ImmutableMap.of("app", service.getName()))
		        		.withType("NodePort")
		        		.addNewPort()
			        		.withName("http-main")
			        		.withPort(service.getServicePort())
			        		.withTargetPort(new IntOrString(service.getServicePort()))
			        		.withProtocol("TCP")
		        		.endPort()
		        .endSpec()
		        
			.build();
		if(null != service.getSidecarPort()) {
			final List<ServicePort> portList = kubeService.getSpec().getPorts();
			final ServicePort sidecarPort = new ServicePortBuilder().withName("http-sidecar")
	        		.withPort(service.getSidecarPort())
	        		.withTargetPort(new IntOrString(service.getSidecarPort()))
	        		.withProtocol("TCP").build();
			portList.add(sidecarPort);
		}
		logger.info("Created kube service {}", CommonUtil.asJsonString(kubeService));
		final io.fabric8.kubernetes.api.model.Service kubeServiceResult = kClient.services().inNamespace(service.getNamespace()).createOrReplace(kubeService);
        logger.info("Created kube service with status: {}", CommonUtil.asJsonString(kubeServiceResult.getStatus()));
	}
	
	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.infra.ContainerManager#addService(com.anishsneh.microweaver.service.core.vo.Service)
	 */
	@Override
	public void addService(final Service service){
		try {
			final KubernetesClient kClient = kubernetesApiHelper.getClient(null);
			createKubernetesDeployment(service, kClient);
			createKubernetesService(service, kClient);
		}
		catch(final Exception ex) {
			logger.error("Failed to deploy kube deployment/service", ex);
			throw new ContainerManagerException(ex.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.infra.ContainerManager#updateService(com.anishsneh.microweaver.service.core.vo.Service)
	 */
	@Override
	public void updateService(final Service service) {
		addService(service);		
	}
	
	/**
	 * Removes the kubernetes service.
	 *
	 * @param service the service
	 * @param kClient the k client
	 * @return true, if successful
	 */
	private boolean removeKubernetesService(final Service service, final KubernetesClient kClient){
		final boolean removed = kClient.services().inNamespace(service.getNamespace()).withName(service.getName()).delete();
		return removed;
	}
	
	/**
	 * Removes the kubernetes deployment.
	 *
	 * @param service the service
	 * @param kClient the k client
	 * @return true, if successful
	 */
	private boolean removeKubernetesDeployment(final Service service, final KubernetesClient kClient){
		final boolean removed = kClient.extensions().deployments().inNamespace(service.getNamespace()).withName(service.getName()).delete();
		return removed;
	}

	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.infra.ContainerManager#removeService(com.anishsneh.microweaver.service.core.vo.Service)
	 */
	@Override
	public void removeService(final Service service) {
		boolean removedService = false;
		boolean removedDeployment = false;
		try {
			final KubernetesClient kClient = kubernetesApiHelper.getClient(null);
			removedService = removeKubernetesService(service, kClient);
			removedDeployment = removeKubernetesDeployment(service, kClient);
		}
		catch(final Exception ex) {
			logger.error("Failed to remove kube deployment/service", ex);
			throw new ContainerManagerException(ex.getMessage());
		}	
		if(!(removedService && removedDeployment)) {
			throw new ContainerManagerException(String.format("Failed to undeploy from kube system, delete status service=[%s], deployment=[{%s}]", removedService + "", removedDeployment + ""));
		}
	}
}

package com.anishsneh.microweaver.service.core.config;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.anishsneh.microweaver.service.core.dao.ServiceDao;
import com.anishsneh.microweaver.service.core.helper.KubernetesApiHelper;
import com.anishsneh.microweaver.service.core.jpa.entity.ServiceEntity;
import com.anishsneh.microweaver.service.core.util.ApiContants;
import com.anishsneh.microweaver.service.core.util.BootstrapUtil;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * The Class MicroweaverCoreStartupHook.
 * 
 * @author Anish Sneh
 * 
 */
@Component
public class MicroweaverCoreStartupHook {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(MicroweaverCoreStartupHook.class);

	/** The service dao. */
	@Autowired
	private ServiceDao serviceDao;
	
	/** The kubernetes api helper. */
	@Autowired
	private KubernetesApiHelper kubernetesApiHelper;

	/**
	 * On context refresh.
	 */
	@EventListener({ ContextRefreshedEvent.class })
	public void onContextRefresh() {
		logger.info("Context refreshed");
		loadBootstrapServiceToServiceDb();
	}

	/**
	 * Load bootstrap service to service db.
	 */
	private void loadBootstrapServiceToServiceDb() {
		final KubernetesClient kClient = kubernetesApiHelper.getClient(null);
		final ConfigMap masterConfigMap = kClient.configMaps().inNamespace(ApiContants.SYSTEM_NAMESPACE).withName(ApiContants.SYSTEM_CONFIGMAP_MASTER).get();
		Map<String, String> configData = BootstrapUtil.getFilteredConfigData(masterConfigMap.getData(), null);
		for(final Map.Entry<String, String> configEntry : configData.entrySet()) {
			logger.info(configEntry.getKey() + "=" + configEntry.getValue());
		}
		logger.info(BootstrapUtil.getBootstrapDeployments(configData).toString());
		
		final List<String> deploymentIdList = BootstrapUtil.getBootstrapDeployments(configData);
		for(final String deploymentId : deploymentIdList) {
			try {
				final ServiceEntity serviceEntity = new ServiceEntity();
				serviceEntity.setActive(true);
				serviceEntity.setIdx(BootstrapUtil.getDeploymentIndex(configData, deploymentId));
				serviceEntity.setDescription("Bootstrap system service");
				serviceEntity.setImageName(BootstrapUtil.getDeploymentImageName(configData, deploymentId));
				serviceEntity.setImageTag(BootstrapUtil.getDeploymentImageTag(configData, deploymentId));
				serviceEntity.setName(BootstrapUtil.getDeploymentName(configData, deploymentId));
				serviceEntity.setNamespace(BootstrapUtil.getDeploymentNamespace(configData));
				serviceEntity.setRegistryUrl(BootstrapUtil.getImageRegistryUrl(configData));
				serviceEntity.setReplicas(BootstrapUtil.getDeploymentReplicas(configData, deploymentId));
				serviceEntity.setRequestTimeoutSeconds(BootstrapUtil.getBootstrapRequestTimeout());
				serviceEntity.setServicePort(BootstrapUtil.getDeploymentPort(configData, deploymentId));
				serviceEntity.setServiceType(BootstrapUtil.getDeploymentType(configData, deploymentId));
				serviceEntity.setSidecarPort(BootstrapUtil.getDeploymentSidecarPort(configData, deploymentId));
				serviceDao.createService(serviceEntity);
				logger.info("Persisted [{}]", serviceEntity.toString());
			}
			catch(final Exception ex) {
				logger.error("Error in loading bootstrap service [{}] to db", deploymentId, ex);
			}
		}
	}
}

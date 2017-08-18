package com.anishsneh.microweaver.service.core.helper;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.anishsneh.microweaver.service.core.util.ApiContants;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

/**
 * The Class KubernetesApiHelper.
 * 
 * @author Anish Sneh
 * 
 */
@Component
public class KubernetesApiHelper {
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(KubernetesApiHelper.class);

	/**
	 * Gets the client.
	 *
	 * @param url the url
	 * @return the client
	 */
	public KubernetesClient getClient(final String url) {
		KubernetesClient kClient = null;
		String connUrl = null;
		if(null == url) {
			connUrl = ApiContants.SYSTEM_POD_INTERNAL_API_URL;
			kClient = new DefaultKubernetesClient(connUrl);
			try {
				final String token = new String(Files.readAllBytes(Paths.get(ApiContants.SYSTEM_POD_INTERNAL_OAUTH_TOKEN_FILE)));
				kClient.getConfiguration().setOauthToken(token);
			} 
			catch (final Exception e) {
				logger.error("Failed to get kubernetes client", e);
			}
		}
		else {
			connUrl = url;
			kClient = new DefaultKubernetesClient(connUrl);
		}
		logger.info("Kubernetes REST client object [{}]", kClient);
		return kClient;
	}
}

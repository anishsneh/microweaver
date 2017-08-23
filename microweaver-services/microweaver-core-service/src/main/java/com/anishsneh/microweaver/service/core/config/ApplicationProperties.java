package com.anishsneh.microweaver.service.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The Class ApplicationProperties.
 * 
 * @author Anish Sneh
 * 
 */
@Component
@ConfigurationProperties
public class ApplicationProperties {

	/** The kubernetes url. */
	@Value("${application.kubernetes.url}")
	private String kubernetesUrl;
	
	/** The kubernetes probes initial delay seconds. */
	@Value("${application.kubernetes.probes.initialDelaySeconds}")
	private int kubernetesProbesInitialDelaySeconds;

	/** The kubernetes probes failure threshold. */
	@Value("${application.kubernetes.probes.failureThreshold}")
	private int kubernetesProbesFailureThreshold;
	
	/** The kubernetes auth token path. */
	@Value("${application.kubernetes.authTokenPath}")
	private String kubernetesAuthTokenPath;
	
	/** The system load bootstrap services to db. */
	@Value("${application.system.loadBootstrapServicesToDb}")
	private boolean systemLoadBootstrapServicesToDb;
	
	/**
	 * Gets the kubernetes url.
	 *
	 * @return the kubernetes url
	 */
	public final String getKubernetesUrl() {
		return kubernetesUrl;
	}

	/**
	 * Gets the kubernetes auth token path.
	 *
	 * @return the kubernetes auth token path
	 */
	public final String getKubernetesAuthTokenPath() {
		return kubernetesAuthTokenPath;
	}
	
	/**
	 * Checks if is system load bootstrap services to db.
	 *
	 * @return true, if is system load bootstrap services to db
	 */
	public final boolean isSystemLoadBootstrapServicesToDb() {
		return systemLoadBootstrapServicesToDb;
	}
	
	/**
	 * Gets the kubernetes probes initial delay seconds.
	 *
	 * @return the kubernetes probes initial delay seconds
	 */
	public final int getKubernetesProbesInitialDelaySeconds() {
		return kubernetesProbesInitialDelaySeconds;
	}

	/**
	 * Gets the kubernetes probes failure threshold.
	 *
	 * @return the kubernetes probes failure threshold
	 */
	public final int getKubernetesProbesFailureThreshold() {
		return kubernetesProbesFailureThreshold;
	}
}

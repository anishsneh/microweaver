package com.anishsneh.microweaver.service.sidecar.config;

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

	/** The system name. */
	@Value("${application.system.name}")
	private String systemName;

	/**
	 * Gets the system name.
	 *
	 * @return the system name
	 */
	public String getSystemName() {
		return systemName;
	}
}

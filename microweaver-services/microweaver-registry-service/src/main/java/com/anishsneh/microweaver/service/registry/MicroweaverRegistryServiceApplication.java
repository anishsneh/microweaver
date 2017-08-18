package com.anishsneh.microweaver.service.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * The Class MicroweaverRegistryServiceApplication.
 * 
 * @author Anish Sneh
 * 
 */
@SpringBootApplication
@EnableEurekaServer
public class MicroweaverRegistryServiceApplication {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(MicroweaverRegistryServiceApplication.class, args);
	}
}

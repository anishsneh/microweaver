package com.anishsneh.microweaver.service.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import de.codecentric.boot.admin.config.EnableAdminServer;

/**
 * The Class MicroweaverAdminServiceApplication.
 * 
 * @author Anish Sneh
 * 
 */
@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class MicroweaverAdminServiceApplication {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(MicroweaverAdminServiceApplication.class, args);
	}
}

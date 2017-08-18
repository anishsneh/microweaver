package com.anishsneh.microweaver.service.sidecar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.sidecar.EnableSidecar;

/**
 * The Class MicroweaverSidecarServiceApplication.
 * 
 * @author Anish Sneh
 * 
 */
// e.g. http://10.254.35.41:9165/api/sidecar-service/v1.0/hello
@SpringBootApplication
@EnableEurekaClient
@EnableSidecar
public class MicroweaverSidecarServiceApplication {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(MicroweaverSidecarServiceApplication.class, args);
	}
}

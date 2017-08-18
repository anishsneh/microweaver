package com.anishsneh.microweaver.service.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The Class MicroweaverCoreServiceApplication.
 * 
 * @author Anish Sneh
 * 
 */
@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@EntityScan
public class MicroweaverCoreServiceApplication {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(MicroweaverCoreServiceApplication.class, args);
	}
}

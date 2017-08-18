package com.anishsneh.microweaver.service.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The Class MicroweaverHelloWorldServiceApplication.
 * 
 * @author Anish Sneh
 * 
 */
@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
public class MicroweaverHelloWorldServiceApplication {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(MicroweaverHelloWorldServiceApplication.class, args);
	}
}

package com.anishsneh.microweaver.service.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * The Class MicroweaverGatewayServiceApplication.
 * 
 * @author Anish Sneh
 * 
 */
@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class MicroweaverGatewayServiceApplication {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	// e.g. http://10.254.164.236:9165/api/core-service/v1.0/core/hello
	public static void main(String[] args) {
		SpringApplication.run(MicroweaverGatewayServiceApplication.class, args);
	}
}

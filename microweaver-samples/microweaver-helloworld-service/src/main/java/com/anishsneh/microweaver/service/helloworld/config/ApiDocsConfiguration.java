package com.anishsneh.microweaver.service.helloworld.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.anishsneh.microweaver.service.helloworld.MicroweaverHelloWorldServiceApplication;
import com.anishsneh.microweaver.service.helloworld.util.ApiContants;
import com.anishsneh.microweaver.service.helloworld.util.ApiDocsInfo;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;

/**
 * The Class ApiDocsConfiguration.
 * 
 * @author Anish Sneh
 * 
 */
@Configuration
public class ApiDocsConfiguration {
    
    /**
     * Documentation.
     *
     * @return the docket
     */
    @Bean
    public Docket documentation() {
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
            .apis(RequestHandlerSelectors.basePackage(MicroweaverHelloWorldServiceApplication.class.getPackage().getName()))
            .paths(PathSelectors.any())
            .build()
          .pathMapping("/")
          .apiInfo(metadata());
    }
    
    /**
     * Ui config.
     *
     * @return the ui configuration
     */
    @Bean
    public UiConfiguration uiConfig() {
		return new UiConfiguration(null, new String[]{});
	}
    
    /**
     * Metadata.
     *
     * @return the api info
     */
    private ApiInfo metadata() {
      return new ApiInfoBuilder()
        .title(ApiDocsInfo.TITLE)
        .description(ApiDocsInfo.DESCRIPTION)
        .version(ApiContants.VERSION)
        .contact(new Contact(ApiDocsInfo.CONTACT_NAME, ApiDocsInfo.CONTACT_URL, ApiDocsInfo.CONTACT_EMAIL))
        .build();
    }
}


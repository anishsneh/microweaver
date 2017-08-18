package com.anishsneh.microweaver.service.helloworld.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anishsneh.microweaver.service.helloworld.util.ApiContants;
import com.anishsneh.microweaver.service.helloworld.vo.Message;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;


/**
 * The Class HelloWorldResource.
 * 
 * @author Anish Sneh
 * 
 */
@RestController
@RequestMapping(path = "/v" + ApiContants.VERSION + "/helloworld")
@Api(value = "Service resource", produces = "application/json")
public class HelloWorldResource {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(HelloWorldResource.class);
	
	/**
	 * Gets the hello world.
	 *
	 * @return the hello world
	 */
	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Gets a helloworld resource", response = Message.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "HelloWorld resource found"),
			@ApiResponse(code = 401, message = "You are not authorized to view service"),
	        @ApiResponse(code = 403, message = "Accessing the service you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "Service resource not found") 
			})
	public ResponseEntity<Message> getHelloWorld(){
		final Message message = new Message("Hello World", "SUCCESS");
		logger.info("Retrieved message [{}]", message.toString());
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}
}

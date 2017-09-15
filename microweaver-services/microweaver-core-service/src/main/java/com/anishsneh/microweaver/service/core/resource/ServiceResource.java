package com.anishsneh.microweaver.service.core.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anishsneh.microweaver.service.core.exception.ResourceDataException;
import com.anishsneh.microweaver.service.core.helper.ServiceHelper;
import com.anishsneh.microweaver.service.core.metadata.CreateGroup;
import com.anishsneh.microweaver.service.core.metadata.UpdateGroup;
import com.anishsneh.microweaver.service.core.util.ApiContants;
import com.anishsneh.microweaver.service.core.util.CommonUtil;
import com.anishsneh.microweaver.service.core.vo.Message;
import com.anishsneh.microweaver.service.core.vo.Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

/**
 * The Class ServiceResource.
 * 
 * @author Anish Sneh
 * 
 */
@RestController
@RequestMapping(path = "/v" + ApiContants.VERSION + "/services")
@Api(value = "Service resource", produces = "application/json")
public class ServiceResource {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ServiceResource.class);
	
	/** The service helper. */
	@Autowired
	private ServiceHelper serviceHelper;

	/**
	 * Gets the service by id.
	 *
	 * @param id the id
	 * @return the service by id
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Gets a service resource", response = Service.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Service resource found"),
			@ApiResponse(code = 401, message = "You are not authorized to view service"),
	        @ApiResponse(code = 403, message = "Accessing the service you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "Service resource not found") 
			})
	public ResponseEntity<Service> getServiceById(
			@ApiParam(value = "Service id to be retrieved", required = true, example = "23") 
			@PathVariable final long id
			){
		final Service service = serviceHelper.validateServiceId(id);
		logger.info("Retrieved service: " + service.toString());
		return new ResponseEntity<Service>(service, HttpStatus.OK);
	}
	
	/**
	 * Creates the service.
	 *
	 * @param service the service
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Creates a service resource", response = Service.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 201, message = "Service resource created"),
			@ApiResponse(code = 401, message = "You are not authorized to view service"),
	        @ApiResponse(code = 403, message = "Accessing the service you were trying to reach is forbidden"),
			})
	public ResponseEntity<Service> createService(
			@RequestBody final Service service
			){
		service.setServiceType(CommonUtil.SERVICE_TYPE_USER);
		CommonUtil.validate(service, CreateGroup.class);
		final Service newService = serviceHelper.createService(service);
		logger.info("Created service: " + newService.toString());
		return new ResponseEntity<Service>(newService, HttpStatus.CREATED);
	}
	
	/**
	 * Delete service by id.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Deletes a service resource", response = Message.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Service resource deleted"),
			@ApiResponse(code = 401, message = "You are not authorized to view service"),
	        @ApiResponse(code = 403, message = "Accessing the service you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "Service resource not found") 
			})
	public ResponseEntity<Message> deleteServiceById(
			@ApiParam(value = "Service id to be deleted", required = true, example = "23") 
			@PathVariable final long id
			){
		final Service service = serviceHelper.validateServiceId(id);
		if(!serviceHelper.isDeletable(service)) {
			throw new ResourceDataException("ER00006", "Deletion of service type SYSTEM is not allowed through API");
		}
		logger.info("Deleting service: " + service.toString());
		serviceHelper.deleteServiceById(service.getId());
		final Message message = new Message("SC00001", "Service deleted with id: " + id);
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}
	
	/**
	 * Update service.
	 *
	 * @param id the id
	 * @param service the service
	 * @param sync the sync
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Updates a service resource", response = Service.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Service resource updated"),
			@ApiResponse(code = 401, message = "You are not authorized to view service"),
	        @ApiResponse(code = 403, message = "Accessing the service you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "Service resource not found") 
			})
	public ResponseEntity<Service> updateService(
			@ApiParam(value = "Service id to be updated", required = true, example = "25") 
			@PathVariable final long id,
			@RequestBody final Service service,
			@ApiParam(value = "true, if sync required; it will include activate flag as well otherwise ignored", required = true, example = "true")
			@RequestParam(name = "sync", required = false) boolean sync
			){
		serviceHelper.validateServiceId(id);
		CommonUtil.validate(service, UpdateGroup.class);
		logger.info("Retrieved service payload for update: " + service.toString());
		final Service updatedService = serviceHelper.updateService(id, service, sync);
		return new ResponseEntity<Service>(updatedService, HttpStatus.OK);
	}
}

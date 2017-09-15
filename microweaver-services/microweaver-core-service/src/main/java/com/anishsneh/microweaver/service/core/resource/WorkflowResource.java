package com.anishsneh.microweaver.service.core.resource;

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
import com.anishsneh.microweaver.service.core.helper.WorkflowHelper;
import com.anishsneh.microweaver.service.core.metadata.CreateGroup;
import com.anishsneh.microweaver.service.core.metadata.UpdateGroup;
import com.anishsneh.microweaver.service.core.util.ApiContants;
import com.anishsneh.microweaver.service.core.util.CommonUtil;
import com.anishsneh.microweaver.service.core.vo.Message;
import com.anishsneh.microweaver.service.core.vo.Workflow;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.ApiResponse;

/**
 * The Class WorkflowResource.
 * 
 * @author Anish Sneh
 * 
 */
@Slf4j
@RestController
@RequestMapping(path = "/v" + ApiContants.VERSION + "/workflows")
@Api(value = "Workflow resource", produces = "application/json")
public class WorkflowResource {

	/** The workflow helper. */
	@Autowired
	private WorkflowHelper workflowHelper;

	/**
	 * Gets the workflow by id.
	 *
	 * @param id the id
	 * @return the workflow by id
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Gets a workflow resource", response = Workflow.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Workflow resource found"),
			@ApiResponse(code = 401, message = "You are not authorized to view workflow"),
	        @ApiResponse(code = 403, message = "Accessing the workflow you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "Workflow resource not found") 
			})
	public ResponseEntity<Workflow> getWorkflowById(
			@ApiParam(value = "Workflow id to be retrieved", required = true, example = "23") 
			@PathVariable final long id
			){
		final Workflow workflow = workflowHelper.validateWorkflowId(id);
		log.info("Retrieved workflow: " + workflow.toString());
		return new ResponseEntity<Workflow>(workflow, HttpStatus.OK);
	}
	
	/**
	 * Creates the workflow.
	 *
	 * @param workflow the workflow
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Creates a workflow resource", response = Workflow.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 201, message = "Workflow resource created"),
			@ApiResponse(code = 401, message = "You are not authorized to view workflow"),
	        @ApiResponse(code = 403, message = "Accessing the workflow you were trying to reach is forbidden"),
			})
	public ResponseEntity<Workflow> createWorkflow(
			@RequestBody final Workflow workflow
			){
		workflow.setWorkflowType(CommonUtil.WORKFLOW_TYPE_USER);
		CommonUtil.validate(workflow, CreateGroup.class);
		final Workflow newWorkflow = workflowHelper.createWorkflow(workflow);
		log.info("Created workflow: " + newWorkflow.toString());
		return new ResponseEntity<Workflow>(newWorkflow, HttpStatus.CREATED);
	}
	
	/**
	 * Delete workflow by id.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Deletes a workflow resource", response = Message.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Workflow resource deleted"),
			@ApiResponse(code = 401, message = "You are not authorized to view workflow"),
	        @ApiResponse(code = 403, message = "Accessing the workflow you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "Workflow resource not found") 
			})
	public ResponseEntity<Message> deleteWorkflowById(
			@ApiParam(value = "Workflow id to be deleted", required = true, example = "23") 
			@PathVariable final long id
			){
		final Workflow workflow = workflowHelper.validateWorkflowId(id);
		if(!workflowHelper.isDeletable(workflow)) {
			throw new ResourceDataException("ER00006", "Deletion of workflow type SYSTEM is not allowed through API");
		}
		log.info("Deleting workflow: " + workflow.toString());
		workflowHelper.deleteWorkflowById(workflow.getId());
		final Message message = new Message("SC00001", "Workflow deleted with id: " + id);
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}
	
	/**
	 * Update workflow.
	 *
	 * @param id the id
	 * @param workflow the workflow
	 * @param sync the sync
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Updates a workflow resource", response = Workflow.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Workflow resource updated"),
			@ApiResponse(code = 401, message = "You are not authorized to view workflow"),
	        @ApiResponse(code = 403, message = "Accessing the workflow you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "Workflow resource not found") 
			})
	public ResponseEntity<Workflow> updateWorkflow(
			@ApiParam(value = "Workflow id to be updated", required = true, example = "25") 
			@PathVariable final long id,
			@RequestBody final Workflow workflow,
			@ApiParam(value = "true, if sync required; it will include activate flag as well otherwise ignored", required = true, example = "true")
			@RequestParam(name = "sync", required = false) boolean sync
			){
		workflowHelper.validateWorkflowId(id);
		CommonUtil.validate(workflow, UpdateGroup.class);
		log.info("Retrieved workflow payload for update: " + workflow.toString());
		final Workflow updatedWorkflow = workflowHelper.updateWorkflow(id, workflow, sync);
		return new ResponseEntity<Workflow>(updatedWorkflow, HttpStatus.OK);
	}
	
	/**
	 * Gets the workflow context by id.
	 *
	 * @param id the id
	 * @return the workflow context by id
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/context", produces = { MediaType.APPLICATION_XML_VALUE })
	@ApiOperation(value = "Gets the context as xml for a workflow resource", response = String.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Workflow resource found"),
			@ApiResponse(code = 401, message = "You are not authorized to view workflow"),
	        @ApiResponse(code = 403, message = "Accessing the workflow you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "Workflow resource not found") 
			})
	public ResponseEntity<String> getWorkflowContextById(
			@ApiParam(value = "Workflow id for context to be retrieved", required = true, example = "23") 
			@PathVariable final long id
			){
		final Workflow workflow = workflowHelper.validateWorkflowId(id);
		log.info("Retrieved workflow: " + workflow.toString());
		return new ResponseEntity<String>(workflowHelper.getWorkflowContextAsXml(workflow), HttpStatus.OK);
	}
}
package com.anishsneh.microweaver.service.core.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.anishsneh.microweaver.service.core.exception.ResourceDataException;
import com.anishsneh.microweaver.service.core.helper.TaskHelper;
import com.anishsneh.microweaver.service.core.metadata.CreateGroup;
import com.anishsneh.microweaver.service.core.metadata.UpdateGroup;
import com.anishsneh.microweaver.service.core.util.ApiContants;
import com.anishsneh.microweaver.service.core.util.CommonUtil;
import com.anishsneh.microweaver.service.core.vo.Message;
import com.anishsneh.microweaver.service.core.vo.Task;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.ApiResponse;

/**
 * The Class TaskResource.
 * 
 * @author Anish Sneh
 * 
 */
@Slf4j
@RestController
@RequestMapping(path = "/v" + ApiContants.VERSION + "/tasks")
@Api(value = "Task resource", produces = "application/json")
public class TaskResource {

	/** The task helper. */
	@Autowired
	private TaskHelper taskHelper;

	/**
	 * Gets the task by id.
	 *
	 * @param id the id
	 * @return the task by id
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Gets a task resource", response = Task.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Task resource found"),
			@ApiResponse(code = 401, message = "You are not authorized to view task"),
	        @ApiResponse(code = 403, message = "Accessing the task you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "Task resource not found") 
			})
	public ResponseEntity<Task> getTaskById(
			@ApiParam(value = "Task id to be retrieved", required = true, example = "23") 
			@PathVariable final long id
			){
		final Task task = taskHelper.validateTaskId(id);
		log.info("Retrieved task: " + task.toString());
		return new ResponseEntity<Task>(task, HttpStatus.OK);
	}
	
	/**
	 * Creates the task.
	 *
	 * @param task the task
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Creates a task resource", response = Task.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 201, message = "Task resource created"),
			@ApiResponse(code = 401, message = "You are not authorized to view task"),
	        @ApiResponse(code = 403, message = "Accessing the task you were trying to reach is forbidden"),
			})
	public ResponseEntity<Task> createTask(
			@RequestBody final Task task
			){
		task.setTaskType(CommonUtil.TASK_TYPE_USER);
		task.setServiceMethod("POST");
		CommonUtil.validate(task, CreateGroup.class);
		final Task newTask = taskHelper.createTask(task);
		log.info("Created task: " + newTask.toString());
		return new ResponseEntity<Task>(newTask, HttpStatus.CREATED);
	}
	
	/**
	 * Delete task by id.
	 *
	 * @param id the id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Deletes a task resource", response = Message.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Task resource deleted"),
			@ApiResponse(code = 401, message = "You are not authorized to view task"),
	        @ApiResponse(code = 403, message = "Accessing the task you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "Task resource not found") 
			})
	public ResponseEntity<Message> deleteTaskById(
			@ApiParam(value = "Task id to be deleted", required = true, example = "23") 
			@PathVariable final long id
			){
		final Task task = taskHelper.validateTaskId(id);
		if(!taskHelper.isDeletable(task)) {
			throw new ResourceDataException("ER00006", "Deletion of task type SYSTEM is not allowed through API");
		}
		log.info("Deleting task: " + task.toString());
		taskHelper.deleteTaskById(task.getId());
		final Message message = new Message("SC00001", "Task deleted with id: " + id);
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}
	
	/**
	 * Update task.
	 *
	 * @param id the id
	 * @param task the task
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Updates a task resource", response = Task.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Task resource updated"),
			@ApiResponse(code = 401, message = "You are not authorized to view task"),
	        @ApiResponse(code = 403, message = "Accessing the task you were trying to reach is forbidden"),
			@ApiResponse(code = 404, message = "Task resource not found") 
			})
	public ResponseEntity<Task> updateTask(
			@ApiParam(value = "Task id to be updated", required = true, example = "25") 
			@PathVariable final long id,
			@RequestBody final Task task
			){
		taskHelper.validateTaskId(id);
		CommonUtil.validate(task, UpdateGroup.class);
		log.info("Retrieved task payload for update: " + task.toString());
		final Task updatedTask = taskHelper.updateTask(id, task);
		return new ResponseEntity<Task>(updatedTask, HttpStatus.OK);
	}
}
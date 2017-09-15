package com.anishsneh.microweaver.service.core.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.anishsneh.microweaver.service.core.dao.TaskDao;
import com.anishsneh.microweaver.service.core.dao.WorkflowDao;
import com.anishsneh.microweaver.service.core.exception.ResourceDataException;
import com.anishsneh.microweaver.service.core.exception.ResourceNotFoundException;
import com.anishsneh.microweaver.service.core.jpa.entity.WorkflowEntity;
import com.anishsneh.microweaver.service.core.mapper.TaskModelMapper;
import com.anishsneh.microweaver.service.core.mapper.WorkflowModelMapper;
import com.anishsneh.microweaver.service.core.util.CommonUtil;
import com.anishsneh.microweaver.service.core.util.WorkflowContextUtil;
import com.anishsneh.microweaver.service.core.vo.Task;
import com.anishsneh.microweaver.service.core.vo.Workflow;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class WorkflowHelper.
 * 
 * @author Anish Sneh
 * 
 */
@Slf4j
@Component
public class WorkflowHelper {
	
	/** The workflow dao. */
	@Autowired
	private WorkflowDao workflowDao;
	
	/** The task dao. */
	@Autowired
	private TaskDao taskDao;
	
	/** The workflow model mapper. */
	@Autowired
	private WorkflowModelMapper workflowModelMapper;
	
	/** The task helper. */
	@Autowired
	private TaskHelper taskHelper;
	
	/** The task model mapper. */
	@Autowired
	private TaskModelMapper taskModelMapper;

	/**
	 * Gets the workflow by id.
	 *
	 * @param id the id
	 * @return the workflow by id
	 */
	public Workflow getWorkflowById(final Long id) {
		log.info("Getting workflow by id [{}]", id);
		final WorkflowEntity workflowEntity = workflowDao.getWorkflowById(id);
		return workflowModelMapper.asWorkflow(workflowEntity);
	}
	
	/**
	 * Creates the workflow.
	 *
	 * @param workflow the workflow
	 * @return the workflow
	 */
	public Workflow createWorkflow(final Workflow workflow) {
		log.info("Creating workflow [{}]", workflow);
		validateTasks(workflow.getTasks());
		final WorkflowEntity workflowEntity = workflowModelMapper.asWorkflowEntity(workflow, true);
		workflowDao.createWorkflow(workflowEntity);
		return workflowModelMapper.asWorkflow(workflowEntity);
	}
	
	/**
	 * Delete workflow by id.
	 *
	 * @param id the id
	 */
	public void deleteWorkflowById(final Long id) {
		log.info("Deleting workflow by id [{}]", id);
		workflowDao.deleteWorkflowById(id);
	}
	
	/**
	 * Validate tasks.
	 *
	 * @param taskList the task list
	 */
	public void validateTasks(final List<String> taskList) {
		if(CollectionUtils.isEmpty(taskList)) {
			log.info("Task list is empty, skipping validation");
			return;
		}
		log.info("Validating [{}] tasks", taskList.toString());
		// TODO Optimize task key validations
		final List<String> invalidTaskList = new ArrayList<>();
		for(final String taskKey : taskList) {
			if(!taskHelper.isValidTask(taskKey)) {
				invalidTaskList.add(taskKey);
			}
		}
		if(!CollectionUtils.isEmpty(invalidTaskList)) {
			throw new ResourceDataException("ER00010", "Failed to find tasks for keys: " + String.join(",", invalidTaskList));
		}
	}
	
	/**
	 * Update workflow.
	 *
	 * @param id the id
	 * @param workflow the workflow
	 * @param sync the sync
	 * @return the workflow
	 */
	public Workflow updateWorkflow(final Long id, final Workflow workflow, final boolean sync) {
		log.info("Updating workflow [{}]", workflow);
		validateTasks(workflow.getTasks());
		final WorkflowEntity workflowEntity = workflowDao.getWorkflowById(id);
		workflowModelMapper.updateWorkflowEntity(workflow, workflowEntity, sync);
		workflowDao.updateWorkflow(workflowEntity, sync);
		return workflowModelMapper.asWorkflow(workflowEntity);
	}
	
	/**
	 * Validate workflow id.
	 *
	 * @param id the id
	 * @return the workflow
	 */
	public Workflow validateWorkflowId(final Long id) {
		log.info("Validating workflow by id [{}]", id);
		final Workflow workflow = this.getWorkflowById(id);
		if(null == workflow) {
			throw new ResourceNotFoundException("ER00002", "Workflow not found with id: " + id, id + "");
		}
		return workflow;
	}
	
	/**
	 * Checks if is deletable.
	 *
	 * @param workflow the workflow
	 * @return true, if is deletable
	 */
	public boolean isDeletable(final Workflow workflow) {
		if(CommonUtil.WORKFLOW_TYPE_SYSTEM.equals(workflow.getWorkflowType())) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the tasks by workflow.
	 *
	 * @param workflow the workflow
	 * @return the tasks by workflow
	 */
	public List<Task> getTasksByWorkflow(final Workflow workflow){
		final List<String> taskKeyList = workflow.getTasks();
		if(CollectionUtils.isEmpty(taskKeyList)) {
			throw new ResourceDataException("ER00011", "Failed to find tasks for workflow: " + workflow.getName());
		}
		final List<Task> taskList = taskKeyList.stream().map(taskKey -> taskModelMapper.asTask(taskDao.getTaskByKey(taskKey))).collect(Collectors.toList());
		return taskList;
	}
	
	/**
	 * Gets the workflow context as xml.
	 *
	 * @param workflow the workflow
	 * @return the workflow context as xml
	 */
	public String getWorkflowContextAsXml(final Workflow workflow) {
		final List<Task> taskList = getTasksByWorkflow(workflow);
		return WorkflowContextUtil.getContextXml(taskList);
	}
}

package com.anishsneh.microweaver.service.core.dao;

import java.util.List;

import com.anishsneh.microweaver.service.core.jpa.entity.WorkflowEntity;

/**
 * The Class WorkflowDao.
 * 
 * @author Anish Sneh
 * 
 */
public interface WorkflowDao {

	/**
	 * Gets the all workflows.
	 *
	 * @return the all workflows
	 */
	public List<WorkflowEntity> getAllWorkflows();
	
	/**
	 * Gets the workflow by id.
	 *
	 * @param id the id
	 * @return the workflow by id
	 */
	public WorkflowEntity getWorkflowById(final Long id);
	
	/**
	 * Creates the workflow.
	 *
	 * @param workflowEntity the workflow entity
	 * @return the workflow entity
	 */
	public WorkflowEntity createWorkflow(final WorkflowEntity workflowEntity);
	
	/**
	 * Delete workflow by id.
	 *
	 * @param id the id
	 */
	public void deleteWorkflowById(final Long id);
	
	/**
	 * Update workflow.
	 *
	 * @param workflowEntity the workflow entity
	 * @param sync the sync
	 */
	public void updateWorkflow(final WorkflowEntity workflowEntity, final boolean sync);
	
	/**
	 * Workflow exists.
	 *
	 * @param id the id
	 * @return true, if successful
	 */
	public boolean workflowExists(final Long id);
}

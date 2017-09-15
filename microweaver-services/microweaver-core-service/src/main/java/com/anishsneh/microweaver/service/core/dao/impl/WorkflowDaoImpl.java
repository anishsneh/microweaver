package com.anishsneh.microweaver.service.core.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anishsneh.microweaver.service.core.dao.WorkflowDao;
import com.anishsneh.microweaver.service.core.jpa.entity.WorkflowEntity;
import com.anishsneh.microweaver.service.core.jpa.repository.WorkflowEntityRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class WorkflowDaoImpl.
 * 
 * @author Anish Sneh
 * 
 */
@Slf4j
@Component
@Transactional
public class WorkflowDaoImpl implements WorkflowDao {
	
	/** The workflow entity repository. */
	@Autowired
	private WorkflowEntityRepository workflowEntityRepository;
	
	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.WorkflowDao#getAllWorkflows()
	 */
	@Override
	public List<WorkflowEntity> getAllWorkflows() {
		log.info("Getting all workflows from repository");
		return workflowEntityRepository.findAll();
	}

	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.WorkflowDao#getWorkflowById(java.lang.Long)
	 */
	@Override
	public WorkflowEntity getWorkflowById(final Long id) {
		log.info("Getting workflow by id from repository");
		final WorkflowEntity workflowEntity = workflowEntityRepository.findById(id);
		return workflowEntity;
	}

	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.WorkflowDao#createWorkflow(com.anishsneh.microweaver.service.core.jpa.entity.WorkflowEntity)
	 */
	@Override
	public WorkflowEntity createWorkflow(final WorkflowEntity workflowEntity) {
		log.info("Creating new workflow using repository");
		workflowEntityRepository.save(workflowEntity);
		return workflowEntity;
	}

	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.WorkflowDao#deleteWorkflowById(java.lang.Long)
	 */
	@Override
	public void deleteWorkflowById(Long id) {
		log.info("Deleting an existing workflow using repository");
		final WorkflowEntity workflowEntity = workflowEntityRepository.findById(id);
		workflowEntityRepository.delete(workflowEntity);		
	}

	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.WorkflowDao#updateWorkflow(com.anishsneh.microweaver.service.core.jpa.entity.WorkflowEntity, boolean)
	 */
	@Override
	public void updateWorkflow(final WorkflowEntity workflowEntity, final boolean sync) {
		log.info("Updating an existing workflow using repository");
		workflowEntityRepository.save(workflowEntity);		
	}

	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.WorkflowDao#workflowExists(java.lang.Long)
	 */
	@Override
	public boolean workflowExists(final Long id) {
		return workflowEntityRepository.exists(id);
	}
}

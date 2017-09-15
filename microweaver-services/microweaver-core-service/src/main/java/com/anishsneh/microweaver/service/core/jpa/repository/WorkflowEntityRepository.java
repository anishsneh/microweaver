package com.anishsneh.microweaver.service.core.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anishsneh.microweaver.service.core.jpa.entity.WorkflowEntity;

/**
 * The Interface WorkflowEntityRepository.
 * 
 * @author Anish Sneh
 * 
 */
public interface WorkflowEntityRepository extends JpaRepository<WorkflowEntity, Long> {
	
	/**
	 * Find by name.
	 *
	 * @param name the name
	 * @return the workflow entity
	 */
	public WorkflowEntity findByName(final String name);
	
	/**
	 * Find by key.
	 *
	 * @param key the key
	 * @return the workflow entity
	 */
	public WorkflowEntity findByKey(final String key);
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the workflow entity
	 */
	public WorkflowEntity findById(final Long id);
}

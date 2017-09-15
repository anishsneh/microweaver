package com.anishsneh.microweaver.service.core.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anishsneh.microweaver.service.core.jpa.entity.TaskEntity;

/**
 * The Interface TaskEntityRepository.
 * 
 * @author Anish Sneh
 * 
 */
public interface TaskEntityRepository extends JpaRepository<TaskEntity, Long> {
	
	/**
	 * Find by name.
	 *
	 * @param name the name
	 * @return the task entity
	 */
	public TaskEntity findByName(final String name);
	
	/**
	 * Find by ekey.
	 *
	 * @param key the ekey
	 * @return the task entity
	 */
	public TaskEntity findByEkey(final String ekey);
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the task entity
	 */
	public TaskEntity findById(final Long id);
}

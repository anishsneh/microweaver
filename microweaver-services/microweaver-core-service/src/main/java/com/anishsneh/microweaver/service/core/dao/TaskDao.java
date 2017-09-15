package com.anishsneh.microweaver.service.core.dao;

import java.util.List;

import com.anishsneh.microweaver.service.core.jpa.entity.TaskEntity;

/**
 * The Class TaskDao.
 * 
 * @author Anish Sneh
 * 
 */
public interface TaskDao {

	/**
	 * Gets the all tasks.
	 *
	 * @return the all tasks
	 */
	public List<TaskEntity> getAllTasks();
	
	/**
	 * Gets the task by id.
	 *
	 * @param id the id
	 * @return the task by id
	 */
	public TaskEntity getTaskById(final Long id);
	
	/**
	 * Gets the task by key.
	 *
	 * @param key the key
	 * @return the task by key
	 */
	public TaskEntity getTaskByKey(final String key);
	
	/**
	 * Creates the task.
	 *
	 * @param taskEntity the task entity
	 * @return the task entity
	 */
	public TaskEntity createTask(final TaskEntity taskEntity);
	
	/**
	 * Delete task by id.
	 *
	 * @param id the id
	 */
	public void deleteTaskById(final Long id);
	
	/**
	 * Update task.
	 *
	 * @param taskEntity the task entity
	 */
	public void updateTask(final TaskEntity taskEntity);
	
	/**
	 * Task exists.
	 *
	 * @param id the id
	 * @return true, if successful
	 */
	public boolean taskExists(final Long id);
}

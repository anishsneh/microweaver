package com.anishsneh.microweaver.service.core.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anishsneh.microweaver.service.core.dao.TaskDao;
import com.anishsneh.microweaver.service.core.exception.ResourceNotFoundException;
import com.anishsneh.microweaver.service.core.jpa.entity.TaskEntity;
import com.anishsneh.microweaver.service.core.mapper.TaskModelMapper;
import com.anishsneh.microweaver.service.core.util.CommonUtil;
import com.anishsneh.microweaver.service.core.vo.Task;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class TaskHelper.
 * 
 * @author Anish Sneh
 * 
 */
@Slf4j
@Component
public class TaskHelper {
	
	/** The task dao. */
	@Autowired
	private TaskDao taskDao;
	
	/** The task model mapper. */
	@Autowired
	private TaskModelMapper taskModelMapper;

	/**
	 * Gets the task by id.
	 *
	 * @param id the id
	 * @return the task by id
	 */
	public Task getTaskById(final Long id) {
		log.info("Getting task by id [{}]", id);
		final TaskEntity taskEntity = taskDao.getTaskById(id);
		return taskModelMapper.asTask(taskEntity);
	}
	
	/**
	 * Creates the task.
	 *
	 * @param task the task
	 * @return the task
	 */
	public Task createTask(final Task task) {
		log.info("Creating task [{}]", task);
		final TaskEntity taskEntity = taskModelMapper.asTaskEntity(task, true);
		taskDao.createTask(taskEntity);
		return taskModelMapper.asTask(taskEntity);
	}
	
	/**
	 * Delete task by id.
	 *
	 * @param id the id
	 */
	public void deleteTaskById(final Long id) {
		log.info("Deleting workflow by id [{}]", id);
		taskDao.deleteTaskById(id);
	}
	
	/**
	 * Update task.
	 *
	 * @param id the id
	 * @param task the task
	 * @return the task
	 */
	public Task updateTask(final Long id, final Task task) {
		log.info("Updating task [{}]", task);
		final TaskEntity taskEntity = taskDao.getTaskById(id);
		taskModelMapper.updateTaskEntity(task, taskEntity);
		taskDao.updateTask(taskEntity);
		return taskModelMapper.asTask(taskEntity);
	}
	
	/**
	 * Validate task id.
	 *
	 * @param id the id
	 * @return the task
	 */
	public Task validateTaskId(final Long id) {
		log.info("Validating task by id [{}]", id);
		final Task task = this.getTaskById(id);
		if(null == task) {
			throw new ResourceNotFoundException("ER00002", "Task not found with id: " + id, id + "");
		}
		return task;
	}
	
	/**
	 * Checks if is valid task.
	 *
	 * @param key the key
	 * @return true, if is valid task
	 */
	public boolean isValidTask(final String key) {
		log.info("Validating task by key [{}]", key);
		final TaskEntity taskEntity = taskDao.getTaskByKey(key);
		if(null != taskEntity) {
			log.info("Valid task key [{}]", key);
			return true;
		}
		log.info("Invalid task key [{}]", key);
		return false;
	}
	
	/**
	 * Checks if is deletable.
	 *
	 * @param task the task
	 * @return true, if is deletable
	 */
	public boolean isDeletable(final Task task) {
		if(CommonUtil.TASK_TYPE_SYSTEM.equals(task.getTaskType())) {
			return false;
		}
		return true;
	}
}

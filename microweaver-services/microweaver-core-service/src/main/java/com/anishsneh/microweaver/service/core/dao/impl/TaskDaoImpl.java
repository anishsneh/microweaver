package com.anishsneh.microweaver.service.core.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anishsneh.microweaver.service.core.dao.TaskDao;
import com.anishsneh.microweaver.service.core.jpa.entity.TaskEntity;
import com.anishsneh.microweaver.service.core.jpa.repository.TaskEntityRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class TaskDaoImpl.
 * 
 * @author Anish Sneh
 * 
 */
@Slf4j
@Component
@Transactional
public class TaskDaoImpl implements TaskDao {
	
	/** The task entity repository. */
	@Autowired
	private TaskEntityRepository taskEntityRepository;
	
	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.TaskDao#getAllTasks()
	 */
	@Override
	public List<TaskEntity> getAllTasks() {
		log.info("Getting all tasks from repository");
		return taskEntityRepository.findAll();
	}

	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.TaskDao#getTaskById(java.lang.Long)
	 */
	@Override
	public TaskEntity getTaskById(final Long id) {
		log.info("Getting task by id from repository");
		final TaskEntity taskEntity = taskEntityRepository.findById(id);
		return taskEntity;
	}
	
	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.TaskDao#getTaskByKey(java.lang.String)
	 */
	@Override
	public TaskEntity getTaskByKey(final String key) {
		log.info("Getting task by key from repository");
		final TaskEntity taskEntity = taskEntityRepository.findByKey(key);
		return taskEntity;
	}

	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.TaskDao#createTask(com.anishsneh.microweaver.service.core.jpa.entity.TaskEntity)
	 */
	@Override
	public TaskEntity createTask(final TaskEntity taskEntity) {
		log.info("Creating new task using repository");
		//TODO Support other methods & read from parameter
		taskEntity.setServiceMethod("POST");
		taskEntityRepository.save(taskEntity);
		return taskEntity;
	}

	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.TaskDao#deleteTaskById(java.lang.Long)
	 */
	@Override
	public void deleteTaskById(final Long id) {
		log.info("Deleting an existing task using repository");
		final TaskEntity taskEntity = taskEntityRepository.findById(id);
		taskEntityRepository.delete(taskEntity);		
	}

	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.TaskDao#updateTask(com.anishsneh.microweaver.service.core.jpa.entity.TaskEntity)
	 */
	@Override
	public void updateTask(final TaskEntity taskEntity) {
		log.info("Updating an existing task using repository");
		//TODO Support other methods & read from parameter
		taskEntity.setServiceMethod("POST");
		taskEntityRepository.save(taskEntity);		
	}

	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.TaskDao#taskExists(java.lang.Long)
	 */
	@Override
	public boolean taskExists(final Long id) {
		return taskEntityRepository.exists(id);
	}
}

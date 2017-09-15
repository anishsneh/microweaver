package com.anishsneh.microweaver.service.core.mapper;

import java.util.Objects;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import com.anishsneh.microweaver.service.core.jpa.entity.TaskEntity;
import com.anishsneh.microweaver.service.core.util.CommonUtil;
import com.anishsneh.microweaver.service.core.vo.Task;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class TaskModelMapper.
 * 
 * @author Anish Sneh
 * 
 */
@Slf4j
@Component
public class TaskModelMapper {
	
	/**
	 * As task.
	 *
	 * @param taskEntity the task entity
	 * @return the task
	 */
	public Task asTask(final TaskEntity taskEntity) {
		if(null == taskEntity) {
			return null;
		}
		final ModelMapper modelMapper = new ModelMapper();
		final PropertyMap<TaskEntity, Task> taskMap = new PropertyMap<TaskEntity, Task>() {
			protected void configure() {				
			}
		};
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.addMappings(taskMap);
		return modelMapper.map(taskEntity, Task.class);
	}

	/**
	 * As task entity.
	 *
	 * @param task the task
	 * @param insert the insert
	 * @return the task entity
	 */
	public TaskEntity asTaskEntity(final Task task, final boolean insert) {
		if(null == task) {
			return null;
		}
		final ModelMapper modelMapper = new ModelMapper();
		final PropertyMap<Task, TaskEntity> taskMap = new PropertyMap<Task, TaskEntity>() {
			protected void configure() {
				if(insert) {
					skip().setId(null);
					skip().setCreatedOn(null);
					skip().setModifiedOn(null);
				}
			}
		};
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.addMappings(taskMap);
		return modelMapper.map(task, TaskEntity.class);
	}
		
	/**
	 * Update task entity.
	 *
	 * @param task the task
	 * @param taskEntity the task entity
	 */
	public void updateTaskEntity(final Task task, final TaskEntity taskEntity) {
		log.info("Before update [{}] [{}]", taskEntity.hashCode(), CommonUtil.asJsonString(taskEntity));
		Objects.requireNonNull(taskEntity);
		Objects.requireNonNull(task);
		final ModelMapper modelMapper = new ModelMapper();
		final PropertyMap<Task, TaskEntity> taskMap = new PropertyMap<Task, TaskEntity>() {
			protected void configure() {
				when(Conditions.isNotNull()).map().setServiceName(source.getServiceName());
				when(Conditions.isNotNull()).map().setServiceMethod(source.getServiceMethod());
				when(Conditions.isNotNull()).map().setServiceUri(source.getServiceUri());
				when(Conditions.isNotNull()).map().setDescription(source.getDescription());
				when(Conditions.isNotNull()).map().setTimeout(source.getTimeout());
				when(Conditions.isNotNull()).map().setQueue(source.getQueue());
				skip().setId(null);
				skip().setEkey(null);
				skip().setTaskType(null);
				skip().setName(null);
				skip().setNamespace(null);
				skip().setCreatedOn(null);
				skip().setModifiedOn(null);
			}
		};
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.addMappings(taskMap);
		modelMapper.map(task, taskEntity);
		log.info("After update [{}] [{}]", taskEntity.hashCode(), CommonUtil.asJsonString(taskEntity));
	}
}

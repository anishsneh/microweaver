package com.anishsneh.microweaver.service.core.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import com.anishsneh.microweaver.service.core.jpa.entity.WorkflowEntity;
import com.anishsneh.microweaver.service.core.util.CommonUtil;
import com.anishsneh.microweaver.service.core.vo.Workflow;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class WorkflowModelMapper.
 * 
 * @author Anish Sneh
 * 
 */
@Slf4j
@Component
public class WorkflowModelMapper {
	
	/**
	 * As workflow.
	 *
	 * @param workflowEntity the workflow entity
	 * @return the workflow
	 */
	public Workflow asWorkflow(final WorkflowEntity workflowEntity) {
		if(null == workflowEntity) {
			return null;
		}
		final ModelMapper modelMapper = new ModelMapper();
		final PropertyMap<WorkflowEntity, Workflow> workflowMap = new PropertyMap<WorkflowEntity, Workflow>() {
			final Converter<String, List<String>> stringToTasksConvertor = new Converter<String, List<String>>() {
				public List<String> convert(MappingContext<String, List<String>> context) {
					final String src = context.getSource();
					return Arrays.asList(src.split(";"));
				}
			};
			protected void configure() {
				when(Conditions.isNotNull()).using(stringToTasksConvertor).map(source.getTasks()).setTasks(null);
			}
		};
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.addMappings(workflowMap);
		return modelMapper.map(workflowEntity, Workflow.class);
	}

	/**
	 * As workflow entity.
	 *
	 * @param workflow the workflow
	 * @param insert the insert
	 * @return the workflow entity
	 */
	public WorkflowEntity asWorkflowEntity(final Workflow workflow, final boolean insert) {
		if(null == workflow) {
			return null;
		}
		final ModelMapper modelMapper = new ModelMapper();
		final PropertyMap<Workflow, WorkflowEntity> workflowMap = new PropertyMap<Workflow, WorkflowEntity>() {
			
			final Converter<List<String>, String> tasksToStringConvertor = new Converter<List<String>, String>() {
				public String convert(MappingContext<List<String>, String> context) {
					final List<String> src = context.getSource();
					return String.join(";", src);
				}
			};
			protected void configure() {
				when(Conditions.isNotNull()).using(tasksToStringConvertor).map(source.getTasks()).setTasks(null);
				if(insert) {
					skip().setId(null);
					skip().setActive(null);
					skip().setCreatedOn(null);
					skip().setModifiedOn(null);
				}
			}
		};
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.addMappings(workflowMap);
		return modelMapper.map(workflow, WorkflowEntity.class);
	}
		
	/**
	 * Update workflow entity.
	 *
	 * @param workflow the workflow
	 * @param workflowEntity the workflow entity
	 * @param force the force
	 */
	public void updateWorkflowEntity(final Workflow workflow, final WorkflowEntity workflowEntity, final boolean force) {
		log.info("Before update [{}] [{}]", workflowEntity.hashCode(), CommonUtil.asJsonString(workflowEntity));
		Objects.requireNonNull(workflowEntity);
		Objects.requireNonNull(workflow);
		final ModelMapper modelMapper = new ModelMapper();
		final PropertyMap<Workflow, WorkflowEntity> workflowMap = new PropertyMap<Workflow, WorkflowEntity>() {
			final Converter<List<String>, String> tasksToStringConvertor = new Converter<List<String>, String>() {
				public String convert(MappingContext<List<String>, String> context) {
					final List<String> src = context.getSource();
					return String.join(";", src);
				}
			};
			protected void configure() {
				when(Conditions.isNotNull()).using(tasksToStringConvertor).map(source.getTasks()).setTasks(null);
				when(Conditions.isNotNull()).map().setWorkflowType(source.getWorkflowType());
				when(Conditions.isNotNull()).map().setDescription(source.getDescription());
				skip().setId(null);
				skip().setName(null);
				skip().setEkey(null);
				skip().setWorkflowType(null);
				skip().setNamespace(null);
				skip().setCreatedOn(null);
				skip().setModifiedOn(null);
				if(!force) {
					skip().setActive(null);
				}
			}
		};
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.addMappings(workflowMap);
		modelMapper.map(workflow, workflowEntity);
		log.info("After update [{}] [{}]", workflowEntity.hashCode(), CommonUtil.asJsonString(workflowEntity));
	}
}

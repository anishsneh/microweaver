package com.anishsneh.microweaver.service.core.mapper;

import java.util.Objects;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.anishsneh.microweaver.service.core.jpa.entity.ServiceEntity;
import com.anishsneh.microweaver.service.core.util.CommonUtil;
import com.anishsneh.microweaver.service.core.vo.Service;

/**
 * The Class ServiceModelMapper.
 * 
 * @author Anish Sneh
 * 
 */
@Component
public class ServiceModelMapper {
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ServiceModelMapper.class);

	/**
	 * As service.
	 *
	 * @param serviceEntity the service entity
	 * @return the service
	 */
	public Service asService(final ServiceEntity serviceEntity) {
		if(null == serviceEntity) {
			return null;
		}
		final ModelMapper modelMapper = new ModelMapper();
		final PropertyMap<ServiceEntity, Service> serviceMap = new PropertyMap<ServiceEntity, Service>() {
			protected void configure() {				
			}
		};
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.addMappings(serviceMap);
		return modelMapper.map(serviceEntity, Service.class);
	}

	/**
	 * As service entity.
	 *
	 * @param service the service
	 * @param insert the insert
	 * @return the service entity
	 */
	public ServiceEntity asServiceEntity(final Service service, final boolean insert) {
		if(null == service) {
			return null;
		}
		final ModelMapper modelMapper = new ModelMapper();
		final PropertyMap<Service, ServiceEntity> serviceMap = new PropertyMap<Service, ServiceEntity>() {
			protected void configure() {
				if(insert) {
					skip().setId(null);
					skip().setActive(null);
					skip().setCreatedOn(null);
					skip().setModifiedOn(null);
				}
			}
		};
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.addMappings(serviceMap);
		return modelMapper.map(service, ServiceEntity.class);
	}
		
	/**
	 * Update service entity.
	 *
	 * @param service the service
	 * @param serviceEntity the service entity
	 * @param force the force
	 */
	public void updateServiceEntity(final Service service, final ServiceEntity serviceEntity, final boolean force) {
		logger.info("Before update [{}] [{}]", serviceEntity.hashCode(), CommonUtil.asJsonString(serviceEntity));
		Objects.requireNonNull(serviceEntity);
		Objects.requireNonNull(service);
		final ModelMapper modelMapper = new ModelMapper();
		final PropertyMap<Service, ServiceEntity> serviceMap = new PropertyMap<Service, ServiceEntity>() {
			protected void configure() {
				when(Conditions.isNotNull()).map().setImageName(source.getImageName());
				when(Conditions.isNotNull()).map().setImageTag(source.getImageTag());
				when(Conditions.isNotNull()).map().setRegistryUrl(source.getRegistryUrl());
				when(Conditions.isNotNull()).map().setDescription(source.getDescription());
				when(Conditions.isNotNull()).map().setServicePort(source.getServicePort());
				when(Conditions.isNotNull()).map().setSidecarPort(source.getSidecarPort());
				when(Conditions.isNotNull()).map().setReplicas(source.getReplicas());
				when(Conditions.isNotNull()).map().setIdx(source.getIdx());
				skip().setId(null);
				skip().setName(null);
				skip().setServiceType(null);
				skip().setNamespace(null);
				skip().setCreatedOn(null);
				skip().setModifiedOn(null);
				if(!force) {
					skip().setActive(null);
				}
			}
		};
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		modelMapper.addMappings(serviceMap);
		modelMapper.map(service, serviceEntity);
		logger.info("After update [{}] [{}]", serviceEntity.hashCode(), CommonUtil.asJsonString(serviceEntity));
	}
}

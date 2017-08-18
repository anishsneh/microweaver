package com.anishsneh.microweaver.service.core.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anishsneh.microweaver.service.core.dao.ServiceDao;
import com.anishsneh.microweaver.service.core.infra.ContainerManager;
import com.anishsneh.microweaver.service.core.jpa.ServiceEntityRepository;
import com.anishsneh.microweaver.service.core.jpa.entity.ServiceEntity;
import com.anishsneh.microweaver.service.core.mapper.ServiceModelMapper;

/**
 * The Class ServiceDaoImpl.
 * 
 * @author Anish Sneh
 * 
 */
@Component
@Transactional
public class ServiceDaoImpl implements ServiceDao {
	
	/** The service entity repository. */
	@Autowired
	private ServiceEntityRepository serviceEntityRepository;
	
	/** The container manager. */
	@Autowired
	private ContainerManager containerManager;
	
	/** The service model mapper. */
	@Autowired
	private ServiceModelMapper serviceModelMapper;
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ServiceDaoImpl.class);
	
	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.ServiceDao#getAllServices()
	 */
	@Override
	public List<ServiceEntity> getAllServices() {
		logger.info("Getting all services from repository");
		return serviceEntityRepository.findAll();
	}

	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.ServiceDao#getServiceById(long)
	 */
	@Override
	public ServiceEntity getServiceById(final long id) {
		logger.info("Getting service by id from repository");
		final ServiceEntity serviceEntity = serviceEntityRepository.findById(id);
		return serviceEntity;
	}

	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.ServiceDao#createService(com.anishsneh.microweaver.service.core.jpa.entity.ServiceEntity)
	 */
	@Override
	public ServiceEntity createService(final ServiceEntity serviceEntity) {
		logger.info("Creating new service using repository");
		serviceEntity.setActive(false);
		serviceEntityRepository.save(serviceEntity);
		return serviceEntity;
	}

	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.ServiceDao#deleteServiceById(long)
	 */
	@Override
	public void deleteServiceById(long id) {
		logger.info("Deleting an existing service using repository");
		final ServiceEntity serviceEntity = serviceEntityRepository.findById(id);
		containerManager.removeService(serviceModelMapper.asService(serviceEntity));
		serviceEntityRepository.delete(serviceEntity);		
	}

	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.ServiceDao#updateService(com.anishsneh.microweaver.service.core.jpa.entity.ServiceEntity, boolean)
	 */
	@Override
	public void updateService(final ServiceEntity serviceEntity, final boolean sync) {
		logger.info("Updating an existing service using repository");
		serviceEntityRepository.save(serviceEntity);		
		if(sync) {
			containerManager.updateService(serviceModelMapper.asService(serviceEntity));
		}
	}

	/* (non-Javadoc)
	 * @see com.anishsneh.microweaver.service.core.dao.ServiceDao#serviceExists(long)
	 */
	@Override
	public boolean serviceExists(long id) {
		return serviceEntityRepository.exists(id);
	}
}

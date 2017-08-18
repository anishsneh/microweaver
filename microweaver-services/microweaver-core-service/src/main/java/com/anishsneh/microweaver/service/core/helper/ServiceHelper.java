package com.anishsneh.microweaver.service.core.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anishsneh.microweaver.service.core.dao.ServiceDao;
import com.anishsneh.microweaver.service.core.exception.ResourceNotFoundException;
import com.anishsneh.microweaver.service.core.jpa.entity.ServiceEntity;
import com.anishsneh.microweaver.service.core.mapper.ServiceModelMapper;
import com.anishsneh.microweaver.service.core.util.CommonUtil;
import com.anishsneh.microweaver.service.core.vo.Service;

/**
 * The Class ServiceHelper.
 * 
 * @author Anish Sneh
 * 
 */
@Component
public class ServiceHelper {
	
	/** The service dao. */
	@Autowired
	private ServiceDao serviceDao;
	
	/** The service model mapper. */
	@Autowired
	private ServiceModelMapper serviceModelMapper;

	/**
	 * Gets the service by id.
	 *
	 * @param id the id
	 * @return the service by id
	 */
	public Service getServiceById(final long id) {
		final ServiceEntity serviceEntity = serviceDao.getServiceById(id);
		return serviceModelMapper.asService(serviceEntity);
	}
	
	/**
	 * Creates the service.
	 *
	 * @param service the service
	 * @return the service
	 */
	public Service createService(final Service service) {
		final ServiceEntity serviceEntity = serviceModelMapper.asServiceEntity(service, true);
		serviceDao.createService(serviceEntity);
		return serviceModelMapper.asService(serviceEntity);
	}
	
	/**
	 * Delete service by id.
	 *
	 * @param id the id
	 */
	public void deleteServiceById(final long id) {
		serviceDao.deleteServiceById(id);
	}
	
	/**
	 * Update service.
	 *
	 * @param id the id
	 * @param service the service
	 * @param sync the sync
	 * @return the service
	 */
	public Service updateService(final long id, final Service service, final boolean sync) {
		final ServiceEntity serviceEntity = serviceDao.getServiceById(id);
		serviceModelMapper.updateServiceEntity(service, serviceEntity, sync);
		serviceDao.updateService(serviceEntity, sync);
		return serviceModelMapper.asService(serviceEntity);
	}
	
	/**
	 * Validate service id.
	 *
	 * @param id the id
	 * @return the service
	 */
	public Service validateServiceId(final long id) {
		final Service service = this.getServiceById(id);
		if(null == service) {
			throw new ResourceNotFoundException("ER00002", "Service not found with id: " + id, id);
		}
		return service;
	}
	
	/**
	 * Checks if is deletable.
	 *
	 * @param service the service
	 * @return true, if is deletable
	 */
	public boolean isDeletable(final Service service) {
		if(CommonUtil.SERVICE_TYPE_SYSTEM.equals(service.getServiceType())) {
			return false;
		}
		return true;
	}
}

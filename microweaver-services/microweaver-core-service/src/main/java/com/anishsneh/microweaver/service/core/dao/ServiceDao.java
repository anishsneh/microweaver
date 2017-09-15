package com.anishsneh.microweaver.service.core.dao;

import java.util.List;

import com.anishsneh.microweaver.service.core.jpa.entity.ServiceEntity;

/**
 * The Interface ServiceDao.
 * 
 * @author Anish Sneh
 * 
 */
public interface ServiceDao {

	/**
	 * Gets the all services.
	 *
	 * @return the all services
	 */
	public List<ServiceEntity> getAllServices();
	
	/**
	 * Gets the service by id.
	 *
	 * @param id the id
	 * @return the service by id
	 */
	public ServiceEntity getServiceById(final Long id);
	
	/**
	 * Creates the service.
	 *
	 * @param serviceEntity the service entity
	 * @return the service entity
	 */
	public ServiceEntity createService(final ServiceEntity serviceEntity);
	
	/**
	 * Delete service by id.
	 *
	 * @param id the id
	 */
	public void deleteServiceById(final Long id);
	
	/**
	 * Update service.
	 *
	 * @param serviceEntity the service entity
	 * @param sync the sync
	 */
	public void updateService(final ServiceEntity serviceEntity, final boolean sync);
	
	/**
	 * Service exists.
	 *
	 * @param id the id
	 * @return true, if successful
	 */
	public boolean serviceExists(final Long id);
}

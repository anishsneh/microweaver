package com.anishsneh.microweaver.service.core.infra;

import com.anishsneh.microweaver.service.core.vo.Service;

/**
 * The Interface ContainerManager.
 * 
 * @author Anish Sneh
 * 
 */
public interface ContainerManager {

	/**
	 * Adds the service.
	 *
	 * @param service the service
	 */
	public void addService(final Service service);
	
	/**
	 * Update service.
	 *
	 * @param service the service
	 */
	public void updateService(final Service service);
	
	/**
	 * Removes the service.
	 *
	 * @param service the service
	 */
	public void removeService(final Service service);
}

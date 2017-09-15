package com.anishsneh.microweaver.service.core.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anishsneh.microweaver.service.core.jpa.entity.ServiceEntity;

/**
 * The Interface ServiceEntityRepository.
 * 
 * @author Anish Sneh
 * 
 */
public interface ServiceEntityRepository extends JpaRepository<ServiceEntity, Long> {
	
	/**
	 * Find by name.
	 *
	 * @param name the name
	 * @return the service entity
	 */
	public ServiceEntity findByName(final String name);
	
	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the service entity
	 */
	public ServiceEntity findById(final Long id);
}

package com.anishsneh.microweaver.service.core.exception;

/**
 * The Class ResourceNotFoundException.
 * 
 * @author Anish Sneh
 * 
 */
public class ResourceNotFoundException extends ResourceException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The service id. */
	private long serviceId;
	
	/**
	 * Instantiates a new resource not found exception.
	 *
	 * @param errorCode the error code
	 * @param message the message
	 * @param serviceId the service id
	 */
	public ResourceNotFoundException(final String errorCode, final String message, final long serviceId) {
		super(errorCode, message);
		this.serviceId = serviceId;
	}
	
	/**
	 * Gets the service id.
	 *
	 * @return the service id
	 */
	public long getServiceId() {
		return serviceId;
	}
}

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
	
	/** The resource id. */
	private String resourceId;
	
	/**
	 * Instantiates a new resource not found exception.
	 *
	 * @param errorCode the error code
	 * @param message the message
	 * @param resourceId the resource id
	 */
	public ResourceNotFoundException(final String errorCode, final String message, final String resourceId) {
		super(errorCode, message);
		this.resourceId = resourceId;
	}
	
	/**
	 * Gets the resource id.
	 *
	 * @return the resource id
	 */
	public String getResourceId() {
		return resourceId;
	}
}

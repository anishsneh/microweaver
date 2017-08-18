package com.anishsneh.microweaver.service.core.exception;

/**
 * The Class ResourceDataException.
 * 
 * @author Anish Sneh
 * 
 */
public class ResourceDataException extends ResourceException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new resource data exception.
	 *
	 * @param errorCode the error code
	 * @param message the message
	 */
	public ResourceDataException(final String errorCode, final String message) {
		super(errorCode, message);
	}
}

package com.anishsneh.microweaver.service.core.exception;

/**
 * The Class ResourceException.
 * 
 * @author Anish Sneh
 * 
 */
public class ResourceException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The error code. */
	private String errorCode;
		
	/**
	 * Instantiates a new resource exception.
	 *
	 * @param errorCode the error code
	 * @param message the message
	 */
	public ResourceException(final String errorCode, final String message) {
		super(message);
		this.errorCode = errorCode;
	}
	
	/**
	 * Gets the error code.
	 *
	 * @return the error code
	 */
	public String getErrorCode() {
		return errorCode;
	}
}

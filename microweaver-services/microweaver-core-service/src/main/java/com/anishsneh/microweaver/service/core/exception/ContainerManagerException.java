package com.anishsneh.microweaver.service.core.exception;

/**
 * The Class ContainerManagerException.
 * 
 * @author Anish Sneh
 * 
 */
public class ContainerManagerException extends RuntimeException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new container manager exception.
	 *
	 * @param message the message
	 */
	public ContainerManagerException(final String message) {
		super(message);
	}
}

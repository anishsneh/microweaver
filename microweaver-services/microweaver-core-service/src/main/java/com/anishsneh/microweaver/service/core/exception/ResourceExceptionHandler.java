package com.anishsneh.microweaver.service.core.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.anishsneh.microweaver.service.core.vo.Message;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

/**
 * The Class ResourceExceptionHandler.
 * 
 * @author Anish Sneh
 * 
 */
@RestControllerAdvice
public class ResourceExceptionHandler {
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ResourceExceptionHandler.class);

	/**
	 * Generic exception.
	 *
	 * @param ex the ex
	 * @return the message
	 */
	@ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
    public Message genericException(final Exception ex) {
		logger.error("Exception", ex);
        return new Message("ERROR: " + ex.getMessage(), "ER00001");
    }
	
	/**
	 * Resource not found exception.
	 *
	 * @param ex the ex
	 * @return the message
	 */
	@ExceptionHandler(value = { ResourceNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
    public Message resourceNotFoundException(final ResourceNotFoundException ex) {
		logger.error("ResourceNotFoundException", ex);
        return new Message(ex.getMessage(), ex.getErrorCode());
    }
	
	/**
	 * Resource data exception.
	 *
	 * @param ex the ex
	 * @return the message
	 */
	@ExceptionHandler(value = { ResourceDataException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
    public Message resourceDataException(final ResourceDataException ex) {
		logger.error("ResourceDataException", ex);
        return new Message(ex.getMessage(), ex.getErrorCode());
    }
	
	/**
	 * Constraint violation exception.
	 *
	 * @param ex the ex
	 * @return the message
	 */
	@ExceptionHandler(value = { ConstraintViolationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
    public Message constraintViolationException(final ConstraintViolationException ex) {
		logger.error("ConstraintViolationException", ex);
		final Set<String> messages = new HashSet<>();
	    for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
	        messages.add(violation.getMessage());
	    }
        return new Message(messages.toString(), "ER00004");
    }
	
	/**
	 * Data integrity violation exception.
	 *
	 * @param ex the ex
	 * @return the message
	 */
	//TODO Temporary using dirty way to unwrap data integrity errors, must fix later
	@ExceptionHandler(value = { DataIntegrityViolationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
    public Message dataIntegrityViolationException(final DataIntegrityViolationException ex) {
		logger.error("DataIntegrityViolationException", ex);
		String message = null;
		if(null == ex.getCause()) {
			message = ex.getMessage();
		}
		else {
			if(null == ex.getCause().getCause()) {
				message = ex.getCause().getMessage();
			}
			else {
				message = ex.getCause().getCause().getMessage();
			}
		}
        return new Message(message, "ER00005");
    }
}

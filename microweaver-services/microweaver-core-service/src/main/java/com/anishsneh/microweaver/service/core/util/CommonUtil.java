package com.anishsneh.microweaver.service.core.util;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class CommonUtil.
 * 
 * @author Anish Sneh
 * 
 */
public class CommonUtil {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	
	/** The Constant SERVICE_TYPE_SYSTEM. */
	//TODO Use enum
	public static final String SERVICE_TYPE_SYSTEM = "SYSTEM";
	
	/** The Constant SERVICE_TYPE_USER. */
	public static final String SERVICE_TYPE_USER = "USER";
	
	/**
	 * As json string.
	 *
	 * @param object the object
	 * @return the string
	 */
	public static final String asJsonString(final Object object) {
		if(null == object) {
			return null;
		}
		final ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (final JsonProcessingException ex) {
			logger.error("Failed to get json string", ex);
		}
		return null;
	}
	
	/**
	 * Validate.
	 *
	 * @param <B> the generic type
	 * @param <G> the generic type
	 * @param b the b
	 * @param group the group
	 */
	public static <B, G> void validate(final B b, final Class<G> group) {
		final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<B>> violations = validator.validate(b, group);
		if(null != violations && violations.size() > 0) {
			logger.error("Violations [{}]", violations.toString());
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
		else {
			logger.info("Validated [{}]", b.toString());
		}
	}
}

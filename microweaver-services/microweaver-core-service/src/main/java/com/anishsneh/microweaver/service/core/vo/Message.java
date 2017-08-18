package com.anishsneh.microweaver.service.core.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * The Class Message.
 * 
 * @author Anish Sneh
 * 
 */
@ApiModel
@JsonInclude(Include.NON_NULL)
public class Message {
	
	/** The code. */
	private String code;
	
	/** The message. */
	private String message;
	
	/**
	 * Instantiates a new message.
	 *
	 * @param message the message
	 * @param code the code
	 */
	public Message(final String message, final String code) {
		this.message = message;
		this.code = code;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	@ApiModelProperty(position = 1, required = false, value = "Message text", example = "Successfully deleted service", readOnly = true)
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(final String message) {
		this.message = message;
	}
	
	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	@ApiModelProperty(position = 2, required = false, value = "Message code (can be a success or an error code)", example = "SC00017", readOnly = true)
	public String getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
	public void setCode(final String code) {
		this.code = code;
	}

}

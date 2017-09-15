package com.anishsneh.microweaver.service.core.vo;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import com.anishsneh.microweaver.service.core.metadata.CreateGroup;
import com.anishsneh.microweaver.service.core.metadata.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class Task.
 * 
 * @author Anish Sneh
 * 
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
@JsonInclude(Include.NON_NULL)
public class Task {
	
	/** The id. */
	@ApiModelProperty(position = 1, required = false, value = "Service id", example = "17", readOnly = true)
	private Long id;
	
	/** The name. */
	@ApiModelProperty(position = 2, required = false, value = "Task name", example = "helloworld-task")
	@NotNull(message = "A valid task name is required", groups = { CreateGroup.class })
    @NotBlank(message = "A valid task name is required", groups = { CreateGroup.class })
	@Pattern(regexp = "[a-z\\-A-Z0-9]+", message = "Task name has invalid characters (only a-z, A-Z, 0-9, '-' are allowed)", groups = { CreateGroup.class, UpdateGroup.class })
	private String name;
		
	/** The key. */
	@ApiModelProperty(position = 3, required = false, value = "Task name", example = "hellotask")
	@NotNull(message = "A valid task key is required", groups = { CreateGroup.class })
    @NotBlank(message = "A valid task key is required", groups = { CreateGroup.class })
	@Pattern(regexp = "[a-zA-Z0-9]+", message = "Task key has invalid characters (only a-z, A-Z, 0-9 are allowed)", groups = { CreateGroup.class, UpdateGroup.class })
	private String key;
	
	/** The service name. */
	@ApiModelProperty(position = 4, required = false, value = "Service name", example = "helloworld-service")
	@NotNull(message = "A valid service name is required", groups = { CreateGroup.class, UpdateGroup.class })
    @NotBlank(message = "A valid service name is required", groups = { CreateGroup.class, UpdateGroup.class })
	@Pattern(regexp = "[a-z\\-A-Z0-9]+", message = "Service name has invalid characters (only a-z, A-Z, 0-9, '-' are allowed)", groups = { CreateGroup.class, UpdateGroup.class })
	private String serviceName;
	
	/** The task type. */
	@ApiModelProperty(position = 5, required = false, value = "Task type e.g USER or SYSTEM", example = "USER")
	@NotNull(message = "A valid task type is required", groups = { CreateGroup.class })
	@NotBlank(message = "A valid task type is required", groups = { CreateGroup.class })
	private String taskType;
	
	/** The service uri. */
	@ApiModelProperty(position = 6, required = false, value = "Service uri to be used", example = "/v1.0/helloworld")
	@NotNull(message = "A valid task type is required", groups = { CreateGroup.class, UpdateGroup.class })
	@NotBlank(message = "A valid task type is required", groups = { CreateGroup.class, UpdateGroup.class })
	private String serviceUri;
	
	/** The service method. */
	@ApiModelProperty(position = 7, required = false, value = "Service http method e.g GET, PUT, POST, DELETE", example = "POST")
	@NotNull(message = "A valid task type is required", groups = { CreateGroup.class, UpdateGroup.class })
	@NotBlank(message = "A valid task type is required", groups = { CreateGroup.class, UpdateGroup.class })
	private String serviceMethod;
	
	/** The timeout. */
	@ApiModelProperty(position = 8, required = false, value = "Task request timeout in milliseconds", example = "300000")
	@Min(value = 0, message = "A valid task request timeout must be positive", groups = { CreateGroup.class, UpdateGroup.class })
	private Integer timeout;
	
	/** The queue. */
	@ApiModelProperty(position = 9, required = false, value = "Task name", example = "helloworld-task")
	@NotNull(message = "A valid task name is required", groups = { CreateGroup.class, UpdateGroup.class })
    @NotBlank(message = "A valid task name is required", groups = { CreateGroup.class, UpdateGroup.class })
	@Pattern(regexp = "[a-z\\-A-Z0-9]+", message = "Task name has invalid characters (only a-z, A-Z, 0-9, '-' are allowed)", groups = { CreateGroup.class, UpdateGroup.class })
	private String queue;
	
	/** The namespace. */
	@ApiModelProperty(position = 10, required = false, value = "Task namespace to be used for deployment", example = "helloworld-system")
	@NotNull(message = "A valid task namespace is required", groups = { CreateGroup.class })
    @NotBlank(message = "A valid task namespace is required", groups = { CreateGroup.class })
	@Pattern(regexp = "[a-z\\-A-Z0-9]+", message = "Task namespace has invalid characters (only a-z, A-Z, 0-9, '-' are allowed)", groups = { CreateGroup.class, UpdateGroup.class })
	private String namespace;
	
	/** The description. */
	@ApiModelProperty(position = 11, required = false, value = "Task description", example = "This is task is used for core operations")
	private String description;
	
	/** The created on. */
	@ApiModelProperty(position = 12, required = false, value = "Task creation timestamp")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss a z")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Date createdOn;
	
	/** The modified on. */
	@ApiModelProperty(position = 13, required = false, value = "Task updation timestamp")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss a z")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Date modifiedOn;
}

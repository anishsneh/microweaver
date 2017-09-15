package com.anishsneh.microweaver.service.core.vo;

import java.util.Date;
import java.util.List;

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
 * The Class Workflow.
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
public class Workflow {
	
	/** The id. */
	@ApiModelProperty(position = 1, required = false, value = "Workflow id", example = "17", readOnly = true)
	private Long id;
	
	/** The name. */
	@ApiModelProperty(position = 2, required = false, value = "Workflow name", example = "helloworld-workflow")
	@NotNull(message = "A valid workflow name is required", groups = { CreateGroup.class })
    @NotBlank(message = "A valid workflow name is required", groups = { CreateGroup.class })
	@Pattern(regexp = "[a-z\\-A-Z0-9]+", message = "Workflow name has invalid characters (only a-z, A-Z, 0-9, '-' are allowed)", groups = { CreateGroup.class, UpdateGroup.class })
	private String name;
	
	/** The ekey. */
	@ApiModelProperty(position = 3, required = false, value = "Workflow name", example = "helloworkflow")
	@NotNull(message = "A valid workflow ekey is required", groups = { CreateGroup.class })
    @NotBlank(message = "A valid workflow ekey is required", groups = { CreateGroup.class })
	@Pattern(regexp = "[a-zA-Z0-9]+", message = "Workflow ekey has invalid characters (only a-z, A-Z, 0-9 are allowed)", groups = { CreateGroup.class, UpdateGroup.class })
	private String ekey;
	
	/** The active. */
	@ApiModelProperty(position = 4, required = false, value = "true, if workflow is active; false for registered workflows", example = "true", readOnly = true)
	private Boolean active;
	
	/** The workflow type. */
	@ApiModelProperty(position = 5, required = false, value = "Workflow type e.g USER or SYSTEM", example = "USER")
	@NotNull(message = "A valid workflow type is required", groups = { CreateGroup.class })
	@NotBlank(message = "A valid workflow type is required", groups = { CreateGroup.class })
	private String workflowType;
	
	/** The tasks. */
	@ApiModelProperty(position = 6, required = false, value = "Workflow tasks list, MUST be in order of invocation")
	@NotNull(message = "Valid workflow tasks are required", groups = { CreateGroup.class, UpdateGroup.class })
	private List<String> tasks;
	
	/** The namespace. */
	@ApiModelProperty(position = 10, required = false, value = "Workflow namespace to be used for deployment", example = "helloworld-system")
	@NotNull(message = "A valid workflow namespace is required", groups = { CreateGroup.class })
    @NotBlank(message = "A valid workflow namespace is required", groups = { CreateGroup.class })
	@Pattern(regexp = "[a-z\\-A-Z0-9]+", message = "Workflow namespace has invalid characters (only a-z, A-Z, 0-9, '-' are allowed)", groups = { CreateGroup.class, UpdateGroup.class })
	private String namespace;

	/** The description. */
	@ApiModelProperty(position = 7, required = false, value = "Workflow description", example = "This is workflow is used for core operations")
	private String description;
	
	/** The created on. */
	@ApiModelProperty(position = 8, required = false, value = "Workflow creation timestamp")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss a z")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Date createdOn;
	
	/** The modified on. */
	@ApiModelProperty(position = 9, required = false, value = "Workflow updation timestamp")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss a z")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Date modifiedOn;
}

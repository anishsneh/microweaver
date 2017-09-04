package com.anishsneh.microweaver.service.core.vo;

import com.anishsneh.microweaver.service.core.metadata.CreateGroup;
import com.anishsneh.microweaver.service.core.metadata.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * The Class Service.
 * 
 * @author Anish Sneh
 * 
 */
@ApiModel
@JsonInclude(Include.NON_NULL)
public class Service {
	
	/** The id. */
	private Long id;
	
	/** The name. */
	private String name;
	
	/** The active. */
	private Boolean active;
	
	/** The idx. */
	private Integer idx;
	
	/** The replicas. */
	private Integer replicas;
	
	/** The image name. */
	private String imageName;
	
	/** The image tag. */
	private String imageTag;
	
	/** The registry url. */
	private String registryUrl;
	
	/** The namespace. */
	private String namespace;
	
	/** The service port. */
	private Integer servicePort;
	
	/** The sidecar port. */
	private Integer sidecarPort;
	
	/** The service type. */
	private String serviceType;
	
	/** The description. */
	private String description;
	
	/** The created on. */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss a z")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Date createdOn;
	
	/** The modified on. */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss a z")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Date modifiedOn;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@ApiModelProperty(position = 1, required = false, value = "Service id", example = "17", readOnly = true)
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(final Long id) {
		this.id = id;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	@ApiModelProperty(position = 2, required = false, value = "Service name", example = "helloworld-service")
	@NotNull(message = "A valid service name is required", groups = { CreateGroup.class })
    @NotBlank(message = "A valid service name is required", groups = { CreateGroup.class })
	@Pattern(regexp = "[a-z\\-A-Z0-9]+", message = "A valid service name has invalid characters (only a-z, A-Z, 0-9, '-' are allowed)", groups = { CreateGroup.class, UpdateGroup.class })
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Gets the idx.
	 *
	 * @return the idx
	 */
	@ApiModelProperty(position = 3, required = false, value = "Service deployment index (withing the namespace)", example = "1")
	@NotNull(message = "A valid service index is required", groups = { CreateGroup.class })
	@Min(value = 0, message = "A valid service index must be a positive integer", groups = { CreateGroup.class, UpdateGroup.class })
	public Integer getIdx() {
		return idx;
	}

	/**
	 * Sets the idx.
	 *
	 * @param idx the new idx
	 */
	public void setIdx(final Integer idx) {
		this.idx = idx;
	}

	/**
	 * Gets the replicas.
	 *
	 * @return the replicas
	 */
	@ApiModelProperty(position = 4, required = false, value = "Service replicas", example = "2")
	@NotNull(message = "Valid service replicas is required", groups = { CreateGroup.class })
	@Min(value = 0, message = "Valid service replicas must be a positive integer", groups = { CreateGroup.class, UpdateGroup.class })
	public Integer getReplicas() {
		return replicas;
	}

	/**
	 * Sets the replicas.
	 *
	 * @param replicas the new replicas
	 */
	public void setReplicas(final Integer replicas) {
		this.replicas = replicas;
	}
	
	/**
	 * Gets the image name.
	 *
	 * @return the image name
	 */
	@ApiModelProperty(position = 6, required = false, value = "Service container image", example = "helloworld/helloworld-service")
	@NotNull(message = "A valid service image name is required", groups = { CreateGroup.class })
    @NotBlank(message = "A valid service image name is required", groups = { CreateGroup.class })
	@Pattern(regexp = "[a-z\\-A-Z0-9_/]+", message = "A valid service image name has invalid characters (only a-z, A-Z, 0-9, '-', '_', '/' are allowed)", groups = { CreateGroup.class, UpdateGroup.class })
	public String getImageName() {
		return imageName;
	}

	/**
	 * Sets the image name.
	 *
	 * @param imageName the new image name
	 */
	public void setImageName(final String imageName) {
		this.imageName = imageName;
	}
	
	/**
	 * Gets the image tag.
	 *
	 * @return the image tag
	 */
	@ApiModelProperty(position = 7, required = false, value = "Service container image tag", example = "1.0.0")
	@NotNull(message = "A valid service image tag is required", groups = { CreateGroup.class })
    @NotBlank(message = "A valid service image tag is required", groups = { CreateGroup.class })
	@Pattern(regexp = "[a-z\\-\\.A-Z0-9_]+", message = "A valid service image tag has invalid characters (only a-z, A-Z, 0-9, '-', '_', '.' are allowed)", groups = { CreateGroup.class, UpdateGroup.class })
	public String getImageTag() {
		return imageTag;
	}

	/**
	 * Sets the image tag.
	 *
	 * @param imageTag the new image tag
	 */
	public void setImageTag(final String imageTag) {
		this.imageTag = imageTag;
	}

	/**
	 * Gets the registry url.
	 *
	 * @return the registry url
	 */
	@ApiModelProperty(position = 8, required = false, value = "Service container image registry url including port", example = "registry.foobar.com:5000")
	@NotNull(message = "A valid service registry url is required", groups = { CreateGroup.class })
    @NotBlank(message = "A valid service registry url is required", groups = { CreateGroup.class })
	@Pattern(regexp = "[a-z\\-\\.A-Z0-9_]+:(\\d+)", message = "A valid service registry url is required", groups = { CreateGroup.class, UpdateGroup.class })
	public String getRegistryUrl() {
		return registryUrl;
	}

	/**
	 * Sets the registry url.
	 *
	 * @param registryUrl the new registry url
	 */
	public void setRegistryUrl(final String registryUrl) {
		this.registryUrl = registryUrl;
	}
	
	/**
	 * Gets the namespace.
	 *
	 * @return the namespace
	 */
	@ApiModelProperty(position = 9, required = false, value = "Service namespace to be used for deployment", example = "helloworld-system")
	@NotNull(message = "A valid service namespace is required", groups = { CreateGroup.class })
    @NotBlank(message = "A valid service namespace is required", groups = { CreateGroup.class })
	@Pattern(regexp = "[a-z\\-A-Z0-9]+", message = "A valid service namespace has invalid characters (only a-z, A-Z, 0-9, '-' are allowed)", groups = { CreateGroup.class, UpdateGroup.class })
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Sets the namespace.
	 *
	 * @param namespace the new namespace
	 */
	public void setNamespace(final String namespace) {
		this.namespace = namespace;
	}
	
	/**
	 * Gets the service port.
	 *
	 * @return the service port
	 */
	@ApiModelProperty(position = 10, required = false, value = "Service port number", example = "8380")
	@NotNull(message = "A valid service port is required", groups = { CreateGroup.class })
	@Min(value = 2000, message = "A valid service port must not be less than 2000", groups = { CreateGroup.class, UpdateGroup.class })
	@Max(value = 49000, message = "A valid service port must not be greater than 49000", groups = { CreateGroup.class, UpdateGroup.class })
	public Integer getServicePort() {
		return servicePort;
	}

	/**
	 * Sets the service port.
	 *
	 * @param servicePort the new service port
	 */
	public void setServicePort(final Integer servicePort) {
		this.servicePort = servicePort;
	}

	/**
	 * Gets the sidecar port.
	 *
	 * @return the sidecar port
	 */
	@ApiModelProperty(position = 11, required = false, value = "Service sidecar port number (for non-jvm applications)", example = "4400")
	@Min(value = 2000, message = "A valid service sidecar port must not be less than 2000", groups = { CreateGroup.class, UpdateGroup.class })
	@Max(value = 49000, message = "A valid service sidecar port must not be greater than 49000", groups = { CreateGroup.class, UpdateGroup.class })
	public Integer getSidecarPort() {
		return sidecarPort;
	}

	/**
	 * Sets the sidecar port.
	 *
	 * @param sidecarPort the new sidecar port
	 */
	public void setSidecarPort(final Integer sidecarPort) {
		this.sidecarPort = sidecarPort;
	}

	/**
	 * Gets the service type.
	 *
	 * @return the service type
	 */
	@ApiModelProperty(position = 12, required = false, value = "Service type e.g USER or SYSTEM", example = "USER")
	@NotNull(message = "A valid service type is required", groups = { CreateGroup.class })
	@NotBlank(message = "A valid service type is required", groups = { CreateGroup.class })
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * Sets the service type.
	 *
	 * @param serviceType the new service type
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	/**
	 * Gets the created on.
	 *
	 * @return the created on
	 */
	@ApiModelProperty(position = 13, required = false, value = "Service creation timestamp")
	public Date getCreatedOn() {
		return createdOn;
	}
	
	/**
	 * Sets the created on.
	 *
	 * @param createdOn the new created on
	 */
	public void setCreatedOn(final Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * Gets the modified on.
	 *
	 * @return the modified on
	 */
	@ApiModelProperty(position = 14, required = false, value = "Service updation timestamp")
	public Date getModifiedOn() {
		return modifiedOn;
	}

	/**
	 * Sets the modified on.
	 *
	 * @param modifiedOn the new modified on
	 */
	public void setModifiedOn(final Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	/**
	 * Checks if is active.
	 *
	 * @return the boolean
	 */
	@ApiModelProperty(position = 15, required = false, value = "true, if service is active; false for registered services", example = "true", readOnly = true)
	public Boolean isActive() {
		return active;
	}

	/**
	 * Sets the active.
	 *
	 * @param active the new active
	 */
	public void setActive(final Boolean active) {
		this.active = active;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	@ApiModelProperty(position = 16, required = false, value = "Service description", example = "This is service is used for core operations")
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Service [id=" + id + ", name=" + name + ", active=" + active + ", idx=" + idx + ", replicas=" + replicas
				+ ", imageName=" + imageName + ", imageTag="
				+ imageTag + ", registryUrl=" + registryUrl + ", namespace=" + namespace + ", servicePort="
				+ servicePort + ", sidecarPort=" + sidecarPort + ", serviceType=" + serviceType + ", createdOn="
				+ createdOn + ", modifiedOn=" + modifiedOn + ", description=" + description + "]";
	}
}

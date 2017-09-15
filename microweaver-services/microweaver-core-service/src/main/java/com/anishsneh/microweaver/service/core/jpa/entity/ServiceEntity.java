package com.anishsneh.microweaver.service.core.jpa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

/**
 * The Class ServiceEntity.
 * 
 * @author Anish Sneh
 * 
 */
@Entity
@Table(name="services")
public class ServiceEntity implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Long id;
	
	/** The name. */
	@Column(nullable = false, unique = true)
	private String name;
	
	/** The replicas. */
	@Column
	private Integer replicas;
	
	/** The image name. */
	@Column(nullable = false)
	private String imageName;
	
	/** The image tag. */
	@Column(nullable = false)
	private String imageTag;
	
	/** The registry url. */
	@Column(nullable = false)
	private String registryUrl;
	
	/** The namespace. */
	@Column(nullable = false)
	private String namespace;
	
	/** The service port. */
	@Column(nullable = false)
	private Integer servicePort;
	
	/** The sidecar port. */
	@Column
	private Integer sidecarPort;
	
	/** The service type. */
	@Column
	private String serviceType;
	
	/** The idx. */
	@Column
	private Integer idx;
	
	/** The active. */
	@Column
	private Boolean active;
	
	/** The description. */
	@Column
	private String description;
	
	/** The created on. */
	@Column(updatable = false)
	private Date createdOn;
	
	/** The modified on. */
	@Column
	private Date modifiedOn;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
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
	public String getName() {
		return name;
	}
	
	/**
	 * Checks if is active.
	 *
	 * @return the boolean
	 */
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
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Gets the service type.
	 *
	 * @return the service type
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * Sets the service type.
	 *
	 * @param serviceType the new service type
	 */
	public void setServiceType(final String serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * Gets the idx.
	 *
	 * @return the idx
	 */
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
	 * Gets the description.
	 *
	 * @return the description
	 */
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

	/**
	 * Gets the created on.
	 *
	 * @return the created on
	 */
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
	 * On create.
	 */
	@PrePersist
	public void onCreate() {
		this.createdOn = new Date();
		this.modifiedOn = new Date();
	}
	
	/**
	 * On update.
	 */
	@PreUpdate
	public void onUpdate() {
		this.modifiedOn = new Date();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServiceEntity [id=" + id + ", name=" + name + ", replicas=" + replicas + ", imageName=" + imageName + ", imageTag=" + imageTag + ", registryUrl="
				+ registryUrl + ", namespace=" + namespace + ", servicePort=" + servicePort + ", sidecarPort="
				+ sidecarPort + ", serviceType=" + serviceType + ", idx=" + idx + ", active=" + active
				+ ", description=" + description + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + "]";
	}
}

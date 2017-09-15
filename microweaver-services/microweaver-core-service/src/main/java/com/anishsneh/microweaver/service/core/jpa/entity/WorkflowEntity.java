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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The Class WorkflowEntity.
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
@Entity
@Table(name="workflows")
public class WorkflowEntity implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Long id;
	
	/** The ekey. */
	@Column(nullable = false, unique = true)
	private String ekey;
	
	/** The name. */
	@Column(nullable = false, unique = true)
	private String name;
	
	/** The active. */
	@Column
	private Boolean active;
	
	/** The workflow type. */
	@Column
	private String workflowType;
	
	/** The namespace. */
	@Column(nullable = false)
	private String namespace;
	
	/** The tasks. */
	//TODO Use JPA relationships
	@Column
	private String tasks;
	
	/** The created on. */
	@Column(updatable = false)
	private Date createdOn;
	
	/** The modified on. */
	@Column
	private Date modifiedOn;
	
	/** The description. */
	@Column
	private String description;
	
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
}

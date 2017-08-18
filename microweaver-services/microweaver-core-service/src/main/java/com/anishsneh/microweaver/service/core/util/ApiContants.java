package com.anishsneh.microweaver.service.core.util;

/**
 * The Class ApiContants.
 * 
 * @author Anish Sneh
 * 
 */
public class ApiContants {

	/** The Constant VERSION. */
	public static final String VERSION = "1.0";
	
	/** The Constant SYSTEM_POD_INTERNAL_API_URL. */
	public static final String SYSTEM_POD_INTERNAL_API_URL = "https://kubernetes.default:443";
	
	/** The Constant SYSTEM_NAMESPACE. */
	public static final String SYSTEM_NAMESPACE = "microweaver-system";
	
	/** The Constant SYSTEM_CONFIGMAP_MASTER. */
	public static final String SYSTEM_CONFIGMAP_MASTER = "microweaver-master";
	
	/** The Constant SYSTEM_POD_INTERNAL_OAUTH_TOKEN_FILE. */
	public static final String SYSTEM_POD_INTERNAL_OAUTH_TOKEN_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/token";
}

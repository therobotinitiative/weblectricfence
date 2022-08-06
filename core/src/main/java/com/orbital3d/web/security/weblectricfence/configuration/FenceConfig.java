package com.orbital3d.web.security.weblectricfence.configuration;

/**
 * Configuration properties for Wevlectric Fence
 * 
 * @author msiren
 * @since 0.1
 *
 */
public interface FenceConfig {
	/**
	 * @return Secure context root under which authentication is required and
	 *         permissions are checked. Example app, /app, /app/ or /app/ are
	 *         acceptable
	 */
	String secureContextRoot();
}

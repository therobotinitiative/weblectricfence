package com.orbital3d.web.security.weblectricfence.configuration;

/**
 * Weblectric Fence internal configuration. The library uses this interface as
 * proxy to {@link FenceConfig}. This internal configuration modifies the
 * configuration values as needed and validates them. Note this is meant for the
 * librarys internal use only. In the future this might be implemented as
 * injected proxy.
 * 
 * @author msiren
 * @since 0.1
 *
 */
public interface InternalConfig {
	/**
	 * @return Library secure context root int ANT style path
	 */
	String secureContextRoot();
}

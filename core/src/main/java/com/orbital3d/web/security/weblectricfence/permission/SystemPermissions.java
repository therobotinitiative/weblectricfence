package com.orbital3d.web.security.weblectricfence.permission;

import java.util.Set;

public interface SystemPermissions {
	/**
	 * @return {@link Set} of all permissions available in the system
	 * @throws IllegalArgumentException In case reflection is used
	 * @throws IllegalAccessException   In case reflection is used
	 */
	Set<String> allPermissions() throws IllegalArgumentException, IllegalAccessException;
}

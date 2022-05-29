package com.orbital3d.web.security.weblectricfence.exclude;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Paths excluded from authentication. All paths require authentication unless
 * they are excluded with the bean implementation of this interface. The
 * implementation decides that path comparison format.
 * 
 * @author msiren
 * @since 0.1
 *
 */
public interface ExcludeAuthenticationFilter
{
	/**
	 * Add path to be excluded from authentication check with {@link RequestMethod}
	 * specified. If the {@link RequestMethod} is null the
	 * {@link #isExcluded(String, RequestMethod)} will return true for the path.
	 * regardless of the {@link RequestMethod}.
	 * 
	 * @param path          path to be excluded
	 * @param requestMethod {@link RequestMethod} to match; can be null
	 * @throws IllegalArgumentException If the path is already added with same
	 *                                  {@link RequestMethod} or null
	 */
	void addExcluded(String path, RequestMethod requestMethod);

	/**
	 * @see #addExcluded(String, RequestMethod)
	 * @param excludedPaths {@link Map} of excluded paths and {@link RequestMethod}
	 *                      pairs
	 */
	void addExcluded(Map<String, RequestMethod> excludedPaths);

	/**
	 * Checks if the given path is excluded ignoring the {@link RequestMethod}. If
	 * the path is added with any {@link RequestMethod} this method considers it to
	 * be excluded.
	 * 
	 * @param path path to check
	 * @return true if given path is excluded
	 */
	boolean isExcluded(String path);

	/**
	 * Checks if the path with matching {@link RequestMethod} is excluded.
	 * 
	 * @param path   path to match
	 * @param method {@link RequestMethod} to match
	 * @return true if the path and {@link RequestMethod} matches
	 */
	boolean isExcluded(String path, RequestMethod method);
}

package com.orbital3d.web.security.weblectricfence.authorization;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.web.bind.annotation.RequestMethod;

import com.orbital3d.web.security.weblectricfence.type.Permission;

/**
 * Path {@link Permission} matcher. Used to associated {@link Permission} to
 * path and {@link RequestMethod}. The implementing bean decides how the path
 * matching is performed. This interface is used by the library. The application
 * can provide application specific path matching implementation.
 * 
 * @author msiren
 * @since 0.1
 *
 */
public interface AuthorizationMatcher {
	// Container class for end point that requires permission
	public static final class EndPointContainer {
		private final String path;
		private final RequestMethod method;
		private final Permission permission;

		// Enforce static factory method usage.
		private EndPointContainer(String path, RequestMethod method, Permission permission) {
			this.path = path;
			this.method = method;
			this.permission = permission;
		}

		public String getPath() {
			return path;
		}

		public RequestMethod getMethod() {
			return method;
		}

		public Permission getPermission() {
			return permission;
		}

		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(obj, this, false);
		}

		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this, false);
		}

		/**
		 * 
		 * @param path       Path of the end point. Must not be empty.
		 * @param method     Request method to bound the URI to; can be null if the
		 *                   request method is ignored
		 * @param permission Required {@link Permission}
		 * @return New {@link EndPointContainer} instance
		 * @throws IllegalArgumentException If path is empty or {@link Permission} is
		 *                                  null
		 */
		public static EndPointContainer of(final String path, final RequestMethod method, final Permission permission) {
			if (StringUtils.isAllBlank(path)) {
				throw new IllegalArgumentException("Path must not be empty");
			}
			if (method == null || permission == null) {
				throw new IllegalArgumentException("Permission must not be null");
			}
			return new EndPointContainer(path, method, permission);
		}
	}

	/**
	 * @param endPointContainer Appends {@link EndPointContainer} to the matcher
	 * @throws IllegalArgumentException If endPointContainer is null
	 */
	void append(AuthorizationMatcher.EndPointContainer endPointContainer);

	/**
	 * Gets {@link Permission} associated with the path and {@link RequestMethod}.
	 * The implementation class is responsible for deciding how the path is matched.
	 * 
	 * @param path   Path to match
	 * @param method {@link RequestMethod} to match
	 * @return {@link Permission} if found; null if no match found
	 * @throws IllegalArgumentException If either of the parameter is null or the
	 *                                  path is blank
	 */
	Permission requiredPermission(String path, RequestMethod method);
}

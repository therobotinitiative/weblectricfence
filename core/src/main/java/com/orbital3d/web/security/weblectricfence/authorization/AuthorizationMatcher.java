package com.orbital3d.web.security.weblectricfence.authorization;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import com.orbital3d.web.security.weblectricfence.type.Permission;

public interface AuthorizationMatcher
{
	// Container class for end point that requires permission
	public static final class EndPointContainer
	{
		private String URI;
		private RequestMethod method;
		private Permission permission;

		// Enforce static factory method usage.
		private EndPointContainer(String uRI, RequestMethod method, Permission permission)
		{
			URI = uRI;
			this.method = method;
			this.permission = permission;
		}

		public String getURI()
		{
			return URI;
		}

		public RequestMethod getMethod()
		{
			return method;
		}

		public Permission getPermission()
		{
			return permission;
		}

		/**
		 * 
		 * @param URI        URI of the end point. Must not be empty.
		 * @param method     Request method to bound the URI to; can be null if the
		 *                   request method is ignored
		 * @param permission Required {@link Permission}
		 * @return New {@link EndPointContainer} instance
		 * @throws IllegalArgumentException If URI is empty or {@link Permission} is
		 *                                  null
		 */
		public static EndPointContainer of(final String URI, final RequestMethod method, final Permission permission)
		{
			if (StringUtils.isAllBlank(URI))
			{
				throw new IllegalArgumentException("URI must not be empty");
			}
			if (permission == null)
			{
				throw new IllegalArgumentException("Permission must noy be null");
			}
			return new EndPointContainer(URI, method, permission);
		}
	}

	void append(AuthorizationMatcher.EndPointContainer endPointContainer);

	Permission getRequiredPermission(String URI, RequestMethod method);
}

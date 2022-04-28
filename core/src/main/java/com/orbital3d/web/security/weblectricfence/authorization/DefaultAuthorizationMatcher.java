package com.orbital3d.web.security.weblectricfence.authorization;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import com.orbital3d.web.security.weblectricfence.type.Permission;

/**
 * Default implementation of {@link AuthorizationMatcher}.
 * 
 * @since 0.1
 * @author msiren
 *
 */
public class DefaultAuthorizationMatcher implements AuthorizationMatcher
{
	private Set<AuthorizationMatcher.EndPointContainer> permissionedEndPoints = new HashSet<>();

	private AntPathMatcher antPathMatcher = new AntPathMatcher();

	@Override
	public Permission getRequiredPermission(String URI, RequestMethod method)
	{
		Optional<AuthorizationMatcher.EndPointContainer> permissionEntry = permissionedEndPoints.stream()
				.filter(endPoint -> antPathMatcher.match(endPoint.getURI(), URI) && endPoint.getMethod().equals(method)).findFirst();
		return permissionEntry.isPresent() ? permissionEntry.get().getPermission() : null;
	}

	@Override
	public void append(EndPointContainer endPointContainer)
	{
		permissionedEndPoints.add(endPointContainer);
	}
}

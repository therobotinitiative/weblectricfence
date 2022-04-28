package com.orbital3d.web.security.weblectricfence.type;

import java.util.Set;

/**
 * Subject containing the session related information.
 * 
 * @since 0.1
 * @author msiren
 *
 */
public final class Subject implements WebLectricSubject
{
	private Object identity;
	private Set<Permission> permissions;
	private String authenticationToken;
	private String refreshToken;

	private Subject(Object identity, Set<Permission> permissions, String authenticationToken, String refreshToken)
	{
		this.identity = identity;
		this.permissions = permissions;
		this.authenticationToken = authenticationToken;
		this.refreshToken = refreshToken;
	}

	public Object getIdentity()
	{
		return identity;
	}

	public void setIdentity(Object identity)
	{
		this.identity = identity;
	}

	public Set<Permission> getPermissions()
	{
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions)
	{
		this.permissions = permissions;
	}

	public String getAuthenticationToken()
	{
		return authenticationToken;
	}

	public void setAuthenticationToken(String authenticationToken)
	{
		this.authenticationToken = authenticationToken;
	}

	public String getRefreshToken()
	{
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken)
	{
		this.refreshToken = refreshToken;
	}

	public static Subject of(Object identity, Set<Permission> permissions, String authenticationToken, String refreshToken)
	{
		return new Subject(identity, permissions, authenticationToken, refreshToken);
	}

	public static Subject of(Object identity, Set<Permission> permissions)
	{
		return new Subject(identity, permissions, null, null);
	}

	public static Subject of(Object identity)
	{
		return new Subject(identity, null, null, null);
	}

}

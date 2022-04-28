package com.orbital3d.web.security.weblectricfence.authentication;

import com.orbital3d.web.security.weblectricfence.exception.AuthenticationException;

public interface AuthenticationWorker
{
	public interface Authenticator
	{
		Object authenticate(String userName, String password) throws AuthenticationException;
	}

	public void authenticate(String userName, String password) throws AuthenticationException;

}

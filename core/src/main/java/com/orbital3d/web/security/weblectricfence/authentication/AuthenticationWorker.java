package com.orbital3d.web.security.weblectricfence.authentication;

import com.orbital3d.web.security.weblectricfence.exception.AuthenticationException;
import com.orbital3d.web.security.weblectricfence.type.UserIdentity;

public interface AuthenticationWorker
{
	public interface Authenticator
	{
		UserIdentity authenticate(String userName, String password) throws AuthenticationException;
	}

	public void authenticate(String userName, String password) throws AuthenticationException;

}

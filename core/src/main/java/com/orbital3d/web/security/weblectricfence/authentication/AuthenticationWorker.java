package com.orbital3d.web.security.weblectricfence.authentication;

import com.orbital3d.web.security.weblectricfence.exception.AuthenticationException;
import com.orbital3d.web.security.weblectricfence.type.UserIdentity;

/**
 * Handles the parts of the authentication required by the library. The actual
 * user authentication is delegated to {@link Authenticator} which is a bean
 * provided by the application to provide application specific user
 * authentication.
 * 
 * @author msiren
 * @since 0.1
 *
 */
public interface AuthenticationWorker
{
	/**
	 * This interface is used to decouple the application specific authentication
	 * method4.
	 * 
	 * @author msiren
	 * @since 0.1
	 */
	public interface Authenticator
	{
		/**
		 * Used to authenticate the user credentials. Must return valid
		 * {@link UserIdentity} or throw {@link AuthenticationException} in case the
		 * credentials could not be authenticated.
		 * 
		 * @param userName User name
		 * @param password Password
		 * @return {@link UserIdentity} instance
		 * @throws AuthenticationException If authentication was unsuccessful
		 */
		UserIdentity authenticate(String userName, String password) throws AuthenticationException;
	}

	/**
	 * Authenticates the given credentials. The final credential verification is
	 * delegated to {@link Authenticator} beans.
	 * 
	 * @param userName
	 * @param password
	 * @throws AuthenticationException If credential verification failed
	 */
	public void authenticate(String userName, String password) throws AuthenticationException;

}

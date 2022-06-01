package com.orbital3d.web.security.weblectricfence.authorization;

import com.orbital3d.web.security.weblectricfence.exception.AuthorizationException;
import com.orbital3d.web.security.weblectricfence.type.Permission;
import com.orbital3d.web.security.weblectricfence.type.WebLectricSubject;

/**
 * Handles the library specific aauthorization mechanism. Delegates the final
 * authorization and permission gathering into {@link Authorizer} bean which
 * <b>must</b> be provided by the application.
 * 
 * @author mikko
 * @since 0.1
 *
 */
public interface AuthorizationWorker
{
	/**
	 * Authorization interface. Authorization specific code is delegated to this
	 * interface. Application can implement this interface to work with the
	 * specifics of the application authorization mechanism.
	 * 
	 * @since 0.1
	 * @author msiren
	 *
	 */
	public interface Authorizer
	{
		/**
		 * Retrieve permissions. Permissions <b>must</b> be set for the given subject.
		 * 
		 * @param subject To set the permissions for
		 */
		void gatherPermissions(WebLectricSubject subject);

		/**
		 * Check the {@link WebLectricSubject} authrization for permission.
		 * 
		 * @param subject    {@link WebLectricSubject} to authorize
		 * @param permission {@link Permission} to authorize
		 * @throws AuthorizationException If the {@link WebLectricSubject} is not
		 *                                authorized for {@link Permission}
		 */
		void authorize(WebLectricSubject subject, Permission permission) throws AuthorizationException;
	}

	/**
	 * Authorize current subject for the given {@link Permission}.
	 * 
	 * @param permission {@link Permission} to check authority against
	 * @throws AuthorizationException If the current subject does not have authority
	 */
	void authorize(Permission permission) throws AuthorizationException;

	/**
	 * @param subject {@link WebLectricSubject} to unauthorize
	 */
	void unauthorize(WebLectricSubject subject);
}

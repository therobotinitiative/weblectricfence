package com.orbital3d.web.security.weblectricfence.type;

import java.util.Set;

/**
 * Weblectric Fence specific subject.<br>
 * <br>
 * Defines subject API specific for weblectric fence.
 * 
 * @since 0.1
 * @author msiren
 *
 */
public interface WebLectricSubject
{
	/**
	 * @return Application specific identity of the subject
	 */
	Object getIdentity();

	/**
	 * Set identity for the subject. Using type {@link Object} so it can be anything
	 * that the application chooses.
	 * 
	 * @param identity Application specific identity
	 */
	void setIdentity(Object identity);

	Set<Permission> getPermissions();

	void setPermissions(Set<Permission> permissions);

	/**
	 * @return Authentication token set by
	 *         {@link WebLectricSubject#setAuthenticationToken(String)}
	 */
	String getAuthenticationToken();

	/**
	 * @param authenticationToken Authentication token for the subject
	 */
	void setAuthenticationToken(String authenticationToken);

	/**
	 * @return Refresh token set by
	 *         {@link WebLectricSubject#setRefreshToken(String)}
	 */
	String getRefreshToken();

	/**
	 * @param refreshToken Refresh token for the subject
	 */
	void setRefreshToken(String refreshToken);
}

package com.orbital3d.web.security.weblectricfence.type;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Subject containing the session related information.
 * 
 * @since 0.1
 * @author msiren
 *
 */
public final class Subject implements WebLectricSubject {

	/**
	 * Generated
	 */
	private static final long serialVersionUID = -6930156879126510692L;

	private UserIdentity identity;
	private transient Iterable<Permission> permissions;
	private String authenticationToken;
	private String refreshToken;

	private Subject(UserIdentity identity, Iterable<Permission> permissions, String authenticationToken,
			String refreshToken) {
		this.identity = identity;
		this.permissions = permissions;
		this.authenticationToken = authenticationToken;
		this.refreshToken = refreshToken;
	}

	@Override
	public UserIdentity getIdentity() {
		return identity;
	}

	@Override
	public void setIdentity(UserIdentity identity) {
		this.identity = identity;
	}

	@Override
	public Iterable<Permission> getPermissions() {
		if (permissions != null) {
			return Collections.unmodifiableCollection((Collection<Permission>) permissions);
		}
		return null;
	}

	@Override
	public void setPermissions(Iterable<Permission> permissions) {
		// I'm no good with iterators and collection type so most likely I will revisit
		// this
		this.permissions = permissions != null
				? new CopyOnWriteArrayList<>((Collection<? extends Permission>) permissions)
				: null;
	}

	@Override
	public String getAuthenticationToken() {
		return authenticationToken;
	}

	@Override
	public void setAuthenticationToken(String authenticationToken) {
		this.authenticationToken = authenticationToken;
	}

	@Override
	public String getRefreshToken() {
		return refreshToken;
	}

	@Override
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public static Subject of(UserIdentity identity, Iterable<Permission> permissions, String authenticationToken,
			String refreshToken) {
		return new Subject(identity, permissions, authenticationToken, refreshToken);
	}

	public static Subject of(UserIdentity identity, Iterable<Permission> permissions) {
		return new Subject(identity, permissions, null, null);
	}

	public static Subject of(UserIdentity identity) {
		return new Subject(identity, null, null, null);
	}

}

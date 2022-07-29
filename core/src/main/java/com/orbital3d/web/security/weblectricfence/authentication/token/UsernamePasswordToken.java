package com.orbital3d.web.security.weblectricfence.authentication.token;

import com.orbital3d.web.security.weblectricfence.authentication.AuthenticationToken;

/**
 * Basic implementation for username and password token.
 * 
 * @author msiren
 * @since 0.1
 */
public class UsernamePasswordToken implements AuthenticationToken {
	private final String username;
	private final String password;

	private UsernamePasswordToken(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public static UsernamePasswordToken of(final String username, final String password) {
		return new UsernamePasswordToken(username, password);
	}
}

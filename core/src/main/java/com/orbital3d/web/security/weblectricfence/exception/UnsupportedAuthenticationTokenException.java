package com.orbital3d.web.security.weblectricfence.exception;

import com.orbital3d.web.security.weblectricfence.authentication.AuthenticationToken;

/**
 * Exception is thrown if no supported {@link AuthenticationToken} is found.
 * 
 * @author msiren
 * @since 0.1
 *
 */
public class UnsupportedAuthenticationTokenException extends RuntimeException {

	/**
	 * Generated.
	 */
	private static final long serialVersionUID = -5463062509801355214L;

	public UnsupportedAuthenticationTokenException() {
		super();
	}

	public UnsupportedAuthenticationTokenException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnsupportedAuthenticationTokenException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedAuthenticationTokenException(String message) {
		super(message);
	}

	public UnsupportedAuthenticationTokenException(Throwable cause) {
		super(cause);
	}

}

package com.orbital3d.web.security.weblectricfence.exception;

/**
 * AuthorizationException when a resource is being accessed that require
 * specific permission and the current subject does not have sufficient
 * permissions.
 * 
 * @author msiren
 * @since 0.1
 *
 */
public class AuthorizationException extends RuntimeException {

	/**
	 * Generated
	 */
	private static final long serialVersionUID = 6172278720644034716L;

	public AuthorizationException() {
		super();
	}

	public AuthorizationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthorizationException(String message) {
		super(message);
	}

	public AuthorizationException(Throwable cause) {
		super(cause);
	}

}

package com.orbital3d.web.security.weblectricfence.exception;

/**
 * AuthenticationException is thrown when a resource is being accessed without
 * being authenticated or the requested path is not excluded from authentication
 * chechking.
 * 
 * @author msiren
 * @since 0.1
 *
 */
public class AuthenticationException extends RuntimeException
{

	/**
	 * Generate
	 */
	private static final long serialVersionUID = -5482749998068309779L;

	public AuthenticationException()
	{
		super();
	}

	public AuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AuthenticationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public AuthenticationException(String message)
	{
		super(message);
	}

	public AuthenticationException(Throwable cause)
	{
		super(cause);
	}

}

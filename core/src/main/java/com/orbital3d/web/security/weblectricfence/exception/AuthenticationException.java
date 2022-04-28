package com.orbital3d.web.security.weblectricfence.exception;

public class AuthenticationException extends Exception
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

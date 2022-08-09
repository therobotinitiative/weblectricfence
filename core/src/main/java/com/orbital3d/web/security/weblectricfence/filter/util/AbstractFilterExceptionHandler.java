package com.orbital3d.web.security.weblectricfence.filter.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Convenience class handling filter level exceptions.
 * 
 * @author msiren
 * @since 0.2
 *
 */
public abstract class AbstractFilterExceptionHandler implements FilterExceptionHandler {

	@Override
	public boolean handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) {
		return doHandleException(exception, request, response);
	}

	protected abstract boolean doHandleException(Exception excepton, HttpServletRequest request,
			HttpServletResponse response);

}

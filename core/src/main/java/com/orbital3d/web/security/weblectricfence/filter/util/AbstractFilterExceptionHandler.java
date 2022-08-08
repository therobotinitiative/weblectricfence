package com.orbital3d.web.security.weblectricfence.filter.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

/**
 * Convwnience class handling filter level exceptions.
 * 
 * @author msiren
 * @since 0.2
 *
 */
@Component
public abstract class AbstractFilterExceptionHandler implements FilterExceptionHandler {

	@Override
	public void handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) {
		doHandleException(exception, request, response);
	}

	protected abstract void doHandleException(Exception excepton, HttpServletRequest request,
			HttpServletResponse response);

}

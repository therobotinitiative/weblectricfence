package com.orbital3d.web.security.weblectricfence.filter.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface for filter level exception handler.
 * 
 * @author msiren
 * @since 0.2
 *
 */
public interface FilterExceptionHandler {
	/**
	 * Handle exception.
	 * 
	 * @param exception Exception to handle
	 * @param request   Original request
	 * @param response  Response object
	 * @return true if the exception was handled; false otherwise
	 */
	boolean handleException(Exception exception, HttpServletRequest request, HttpServletResponse response);
}

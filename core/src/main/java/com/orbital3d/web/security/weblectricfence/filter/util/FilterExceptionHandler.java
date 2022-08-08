package com.orbital3d.web.security.weblectricfence.filter.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface for filter level exception handler. If the exception is not handled
 * it should be re-thrown as a cause in {@link RuntimeException}. This behaviour
 * is likely to be changed.
 * 
 * @author msiren
 * @since 0.2
 *
 */
public interface FilterExceptionHandler {
	void handleException(Exception exception, HttpServletRequest request, HttpServletResponse response);
}

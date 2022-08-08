package com.orbital3d.web.security.weblectricfence.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.orbital3d.web.security.weblectricfence.filter.util.FilterExceptionHandler;

/**
 * Filter for handling filter level exception. The exception handling is done in
 * {@link FilterExceptionHandler}. If this bean is not found in the application
 * context the exception behaviour is not changd.
 * 
 * @author msiren
 * @since 0.2
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionFilter extends OncePerRequestFilter {
	@Autowired(required = false)
	FilterExceptionHandler filterExceptionHadler;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception exception) {
			if (filterExceptionHadler != null) {
				filterExceptionHadler.handleException(exception, request, response);
			} else {
				throw exception;
			}
		}
	}

}

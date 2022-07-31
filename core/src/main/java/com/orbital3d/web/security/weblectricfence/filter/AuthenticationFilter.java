package com.orbital3d.web.security.weblectricfence.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import com.orbital3d.web.security.weblectricfence.exception.AuthenticationException;
import com.orbital3d.web.security.weblectricfence.exclude.ExcludeAuthenticationFilter;
import com.orbital3d.web.security.weblectricfence.util.WFUtil;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationFilter extends OncePerRequestFilter {
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFilter.class);

	@Autowired
	private ExcludeAuthenticationFilter excludeFilter;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!excludeFilter.isExcluded(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()))) {
			LOG.trace("Getting {} URI, authenticated", request.getRequestURI());
			if (!WFUtil.isAuthenticated()) {
				throw new AuthenticationException("Not authenticated, path " + request.getRequestURI());
			}
		} else {
			LOG.trace("URI {} excluded from authentication checking", request.getRequestURI());
		}
		filterChain.doFilter(request, response);
	}

}

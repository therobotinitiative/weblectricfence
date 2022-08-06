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
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.orbital3d.web.security.weblectricfence.configuration.InternalConfig;
import com.orbital3d.web.security.weblectricfence.exception.AuthenticationException;
import com.orbital3d.web.security.weblectricfence.util.FenceUtil;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationFilter extends OncePerRequestFilter {
	private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFilter.class);

	@Autowired(required = true)
	private InternalConfig internalConfig;

	private AntPathMatcher pathMatcher = new AntPathMatcher();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (pathMatcher.match(internalConfig.secureContextRoot(), request.getRequestURI())) {
			LOG.trace("Getting {} URI, authenticated", request.getRequestURI());
			if (!FenceUtil.isAuthenticated()) {
				throw new AuthenticationException("Not authenticated, path " + request.getRequestURI());
			}
		} else {
			LOG.trace("URI {} excluded from authentication checking", request.getRequestURI());
		}
		filterChain.doFilter(request, response);
	}

}

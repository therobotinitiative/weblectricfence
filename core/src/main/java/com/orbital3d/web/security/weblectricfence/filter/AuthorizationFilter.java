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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import com.orbital3d.web.security.weblectricfence.authorization.AuthorizationMatcher;
import com.orbital3d.web.security.weblectricfence.authorization.AuthorizationWorker;
import com.orbital3d.web.security.weblectricfence.configuration.InternalConfig;
import com.orbital3d.web.security.weblectricfence.type.Permission;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE - 22)
public class AuthorizationFilter extends OncePerRequestFilter {
	private static final Logger LOG = LoggerFactory.getLogger(AuthorizationFilter.class);

	@Autowired(required = true)
	private AuthorizationMatcher authorizationMatcher;

	@Autowired(required = true)
	private AuthorizationWorker authorizationWorker;

	@Autowired(required = true)
	private InternalConfig internalConfig;

	private AntPathMatcher pathMatcher = new AntPathMatcher();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Permission permission = authorizationMatcher.requiredPermission(request.getRequestURI(),
				RequestMethod.valueOf(request.getMethod()));
		if (permission != null && pathMatcher.match(internalConfig.secureContextRoot(), request.getRequestURI())) {
			LOG.trace("Authoring URI {} requiring permission {}", request.getRequestURI(), permission);
			authorizationWorker.authorize(permission);
		}
		filterChain.doFilter(request, response);
	}

}

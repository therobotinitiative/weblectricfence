package com.orbital3d.web.weblectric.filter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.RequestMethod;

import com.orbital3d.web.security.weblectricfence.authorization.AuthorizationMatcher;
import com.orbital3d.web.security.weblectricfence.authorization.AuthorizationMatcher.EndPointContainer;
import com.orbital3d.web.security.weblectricfence.authorization.AuthorizationWorker;
import com.orbital3d.web.security.weblectricfence.authorization.DefaultAuthorizationMatcher;
import com.orbital3d.web.security.weblectricfence.configuration.InternalConfig;
import com.orbital3d.web.security.weblectricfence.filter.AuthorizationFilter;
import com.orbital3d.web.security.weblectricfence.type.Permission;

class TestAurhorizationFilter {
	private AuthorizationFilter authorizationFilter;
	private AuthorizationMatcher authorizationMatcher;
	private InternalConfig internalConfig;
	private AuthorizationWorker authorizationWorker;

	@BeforeEach
	void prepare() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		authorizationFilter = new AuthorizationFilter();
		authorizationMatcher = new DefaultAuthorizationMatcher();
		internalConfig = mock(InternalConfig.class);
		authorizationWorker = mock(AuthorizationWorker.class);
		wb(authorizationFilter.getClass(), authorizationFilter, authorizationMatcher, "authorizationMatcher");
		wb(authorizationFilter.getClass(), authorizationFilter, internalConfig, "internalConfig");
		wb(authorizationFilter.getClass(), authorizationFilter, authorizationWorker, "authorizationWorker");
	}

	@Test
	void testNormalPermissionCheck() throws IOException, ServletException {
		// mock behaviour
		Permission p = Permission.of("perm");
		authorizationMatcher.append(EndPointContainer.of("/path/perm", RequestMethod.GET, p));
		when(internalConfig.secureContextRoot()).thenReturn("/path/**/**");
		HttpServletRequest request = new MockHttpServletRequest("GET", "/path/perm");
		HttpServletResponse response = new MockHttpServletResponse();
		FilterChain fc = mock(FilterChain.class);
		// run test
		authorizationFilter.doFilter(request, response, fc);
		// verify
		verify(authorizationWorker).authorize(p);
		verify(fc).doFilter(request, response);
	}

	@Test
	void testNoPermissionCheck1() throws IOException, ServletException {
		// mock behaviour
		Permission p = Permission.of("perm");
		authorizationMatcher.append(EndPointContainer.of("/path/perm", RequestMethod.GET, p));
		when(internalConfig.secureContextRoot()).thenReturn("/secure/**/**");
		HttpServletRequest request = new MockHttpServletRequest("GET", "/path/perm");
		HttpServletResponse response = new MockHttpServletResponse();
		FilterChain fc = mock(FilterChain.class);
		// run test
		authorizationFilter.doFilter(request, response, fc);
		// verify
		verify(authorizationWorker, times(0)).authorize(any());
		verify(fc).doFilter(request, response);
	}

	@Test
	void testNoPermissionCheck2() throws IOException, ServletException {
		// mock behaviour
		when(internalConfig.secureContextRoot()).thenReturn("/secure/**/**");
		HttpServletRequest request = new MockHttpServletRequest("GET", "/path/perm");
		HttpServletResponse response = new MockHttpServletResponse();
		FilterChain fc = mock(FilterChain.class);
		// run test
		authorizationFilter.doFilter(request, response, fc);
		// verify
		verify(authorizationWorker, times(0)).authorize(any());
		verify(fc).doFilter(request, response);
	}

	@Test
	void testDifferentRequestMethod() throws IOException, ServletException {
		// mock behaviour
		Permission p = Permission.of("perm");
		authorizationMatcher.append(EndPointContainer.of("/path/perm", RequestMethod.POST, p));
		when(internalConfig.secureContextRoot()).thenReturn("/secure/**/**");
		HttpServletRequest request = new MockHttpServletRequest("GET", "/path/perm");
		HttpServletResponse response = new MockHttpServletResponse();
		FilterChain fc = mock(FilterChain.class);
		// run test
		authorizationFilter.doFilter(request, response, fc);
		// verify
		verify(authorizationWorker, times(0)).authorize(any());
		verify(fc).doFilter(request, response);
	}

	@Test
	void testException1() throws IOException, ServletException {
		// mock behaviour
		Permission p = Permission.of("perm");
		authorizationMatcher.append(EndPointContainer.of("/path/perm", RequestMethod.GET, p));
		when(internalConfig.secureContextRoot()).thenThrow(NullPointerException.class);
		HttpServletRequest request = new MockHttpServletRequest("GET", "/path/perm");
		HttpServletResponse response = new MockHttpServletResponse();
		FilterChain fc = mock(FilterChain.class);
		// run test
		Assertions.assertThrows(NullPointerException.class, () -> {
			authorizationFilter.doFilter(request, response, fc);
		});

		// verify
		verify(authorizationWorker, times(0)).authorize(p);
		verify(fc, times(0)).doFilter(request, response);
	}

	@Test
	void testException2() throws IOException, ServletException {
		// mock behaviour
		Permission p = Permission.of("perm");
		authorizationMatcher.append(EndPointContainer.of("/path/perm", RequestMethod.GET, p));
		when(internalConfig.secureContextRoot()).thenReturn("/path/**/**");
		HttpServletRequest request = new MockHttpServletRequest("GET", "/path/perm");
		HttpServletResponse response = new MockHttpServletResponse();
		FilterChain fc = mock(FilterChain.class);
		doThrow(IllegalArgumentException.class).when(authorizationWorker).authorize(p);
		// run test
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			authorizationFilter.doFilter(request, response, fc);
		});

		// verify
		verify(authorizationWorker, times(1)).authorize(p);
		verify(fc, times(0)).doFilter(request, response);
	}

	private void wb(Class<?> clazz, Object o, Object value, String fn)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = clazz.getDeclaredField(fn);
		f.setAccessible(true);
		f.set(o, value);
	}
}

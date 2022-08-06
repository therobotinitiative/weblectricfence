package com.orbital3d.web.weblectric.filter;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.orbital3d.web.security.weblectricfence.configuration.InternalConfig;
import com.orbital3d.web.security.weblectricfence.exception.AuthenticationException;
import com.orbital3d.web.security.weblectricfence.filter.AuthenticationFilter;
import com.orbital3d.web.security.weblectricfence.util.WFUtil;

public class TestAuthenticationFilter {
	private AuthenticationFilter authenticationFilter;

	@BeforeEach
	public void init() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException,
			NoSuchMethodException, InvocationTargetException {
		InternalConfig ic = mock(InternalConfig.class);

		authenticationFilter = new AuthenticationFilter();
		Field field = AuthenticationFilter.class.getDeclaredField("internalConfig");
		field.setAccessible(true);
		field.set(authenticationFilter, ic);

		when(ic.secureContextRoot()).thenReturn("/path/**/**");
	}

	@Test
	public void testNormalAuthenticatedArray() throws ServletException, IOException {
		List<Pair<String, String>> tests = Arrays.asList(Pair.of("GET", "/path/this/should/require/auth"),
				Pair.of("POST", "/path/this/should/require/auth"),
				Pair.of("GET", "/path?param=this_should_require_auth"), Pair.of("GET", "/path"),
				Pair.of("GET", "/path/"), Pair.of("DELETE", "/path?param=this_should_require_auth"));
		Iterator<Pair<String, String>> iter = tests.iterator();
		while (iter.hasNext()) {
			Pair<String, String> p = iter.next();
			FilterChain filterChain = mock(FilterChain.class);
			HttpServletRequest request = new MockHttpServletRequest(p.getLeft(), p.getRight());
			HttpServletResponse response = new MockHttpServletResponse();
			try (MockedStatic<WFUtil> wfUtilMock = mockStatic(WFUtil.class)) {
				wfUtilMock.when(WFUtil::isAuthenticated).thenReturn(true);
				authenticationFilter.doFilter(request, response, filterChain);
			}
			verify(filterChain).doFilter(eq(request), eq(response));
		}
	}

	@Test
	public void testNormalNotAuthenticated() throws ServletException, IOException {
		List<Pair<String, String>> tests = Arrays.asList(Pair.of("GET", "/path/this/should/require/auth"),
				Pair.of("POST", "/path/this/should/require/auth"), Pair.of("GET", "/path"), Pair.of("GET", "/path/"),
				Pair.of("DELETE", "/path/random/param"));
		Iterator<Pair<String, String>> iter = tests.iterator();
		while (iter.hasNext()) {
			Pair<String, String> p = iter.next();
			FilterChain filterChain = mock(FilterChain.class);
			HttpServletRequest request = new MockHttpServletRequest(p.getLeft(), p.getRight());
			HttpServletResponse response = new MockHttpServletResponse();
			try (MockedStatic<WFUtil> wfUtilMock = mockStatic(WFUtil.class)) {
				wfUtilMock.when(WFUtil::isAuthenticated).thenReturn(false);
				System.out.println("Testing: " + p.getRight());
				Assertions.assertThrows(AuthenticationException.class, () -> {
					authenticationFilter.doFilter(request, response, filterChain);
				});
			}
			verify(filterChain, times(0)).doFilter(eq(request), eq(response));
		}
	}

	@Test

	public void testNoAuthenticationCheck() throws ServletException, IOException {
		List<Pair<String, String>> tests = Arrays.asList(Pair.of("GET", "/public/this/should/not/require/auth"),
				Pair.of("POST", "/public/this/should/not/require/auth"),
				Pair.of("GET", "/public?param=this_should_require_auth"), Pair.of("GET", "/public"),
				Pair.of("GET", "/applicationpath/"), Pair.of("GET", "/public/"), Pair.of("GET", "/favicon.ico"),
				Pair.of("DELETE", "/path?param=this_should_require_auth"), Pair.of("GET", "/"),
				Pair.of("GET", "/login"), Pair.of("GET", "/index"));
		Iterator<Pair<String, String>> iter = tests.iterator();
		while (iter.hasNext()) {
			Pair<String, String> p = iter.next();
			FilterChain filterChain = mock(FilterChain.class);
			HttpServletRequest request = new MockHttpServletRequest(p.getLeft(), p.getRight());
			HttpServletResponse response = new MockHttpServletResponse();
			try (MockedStatic<WFUtil> wfUtilMock = mockStatic(WFUtil.class)) {
				wfUtilMock.when(WFUtil::isAuthenticated).thenReturn(false);
				authenticationFilter.doFilter(request, response, filterChain);
				wfUtilMock.verify(WFUtil::isAuthenticated, times(0));
			}
			verify(filterChain).doFilter(eq(request), eq(response));
		}
	}

	@Test
	public void testException1() throws ServletException, IOException {
		FilterChain filterChain = mock(FilterChain.class);
		HttpServletRequest request = new MockHttpServletRequest("GET", "/path");
		HttpServletResponse response = new MockHttpServletResponse();
		try (MockedStatic<WFUtil> wfUtilMock = mockStatic(WFUtil.class)) {
			wfUtilMock.when(WFUtil::isAuthenticated).thenThrow(NullPointerException.class);
			Assertions.assertThrows(NullPointerException.class, () -> {
				authenticationFilter.doFilter(request, response, filterChain);
			});
		}
		verify(filterChain, times(0)).doFilter(eq(request), eq(response));
	}

	@Test
	public void testException2() throws ServletException, IOException {
		FilterChain filterChain = mock(FilterChain.class);
		HttpServletRequest request = new MockHttpServletRequest("GET", "/path/") {
			public String getRequestURI() {
				throw new NullPointerException();
			};
		};
		HttpServletResponse response = new MockHttpServletResponse();
		try (MockedStatic<WFUtil> wfUtilMock = mockStatic(WFUtil.class)) {
			wfUtilMock.when(WFUtil::isAuthenticated).thenReturn(true);
			Assertions.assertThrows(NullPointerException.class, () -> {
				authenticationFilter.doFilter(request, response, filterChain);
			});
		}
		verify(filterChain, times(0)).doFilter(eq(request), eq(response));
	}

	@Test
	public void testException3() throws ServletException, IOException {
		FilterChain filterChain = mock(FilterChain.class);
		HttpServletRequest request = new MockHttpServletRequest("hilipatsuippa", "/path/") {
			public String getRequestURI() {
				throw new NullPointerException();
			};
		};
		HttpServletResponse response = new MockHttpServletResponse();
		try (MockedStatic<WFUtil> wfUtilMock = mockStatic(WFUtil.class)) {
			wfUtilMock.when(WFUtil::isAuthenticated).thenReturn(true);
			Assertions.assertThrows(NullPointerException.class, () -> {
				authenticationFilter.doFilter(request, response, filterChain);
			});
		}
		verify(filterChain, times(0)).doFilter(eq(request), eq(response));
	}

}

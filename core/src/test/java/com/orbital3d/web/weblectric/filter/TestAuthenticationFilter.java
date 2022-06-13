package com.orbital3d.web.weblectric.filter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
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
import org.mockito.MockedStatic;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.RequestMethod;

import com.orbital3d.web.security.weblectricfence.exception.AuthenticationException;
import com.orbital3d.web.security.weblectricfence.exclude.ExcludeAuthenticationFilter;
import com.orbital3d.web.security.weblectricfence.filter.AuthenticationFilter;
import com.orbital3d.web.security.weblectricfence.util.WFUtil;

public class TestAuthenticationFilter
{
	private ExcludeAuthenticationFilter excludeFilter = null;
	private AuthenticationFilter authenticationFilter;

	@BeforeEach
	public void init() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		excludeFilter = mock(ExcludeAuthenticationFilter.class);
		authenticationFilter = new AuthenticationFilter();
		Field field = AuthenticationFilter.class.getDeclaredField("excludeFilter");
		field.setAccessible(true);
		field.set(authenticationFilter, excludeFilter);
	}

	@Test
	public void testNormalAuthenticated() throws ServletException, IOException
	{
		FilterChain filterChain = mock(FilterChain.class);
		HttpServletRequest request = new MockHttpServletRequest("GET", "/path/");
		HttpServletResponse response = new MockHttpServletResponse();
		when(excludeFilter.isExcluded(anyString(), any(RequestMethod.class))).thenReturn(false);
		try (MockedStatic<WFUtil> wfUtilMock = mockStatic(WFUtil.class))
		{
			wfUtilMock.when(WFUtil::isAuthenticated).thenReturn(true);
			authenticationFilter.doFilter(request, response, filterChain);
		}
		verify(filterChain).doFilter(eq(request), eq(response));
	}

	@Test
	public void testNormalNotAuthenticated() throws ServletException, IOException
	{
		FilterChain filterChain = mock(FilterChain.class);
		HttpServletRequest request = new MockHttpServletRequest("GET", "/path/");
		HttpServletResponse response = new MockHttpServletResponse();
		when(excludeFilter.isExcluded(anyString(), any(RequestMethod.class))).thenReturn(false);
		try (MockedStatic<WFUtil> wfUtilMock = mockStatic(WFUtil.class))
		{
			wfUtilMock.when(WFUtil::isAuthenticated).thenReturn(false);
			Assertions.assertThrows(AuthenticationException.class, () -> {
				authenticationFilter.doFilter(request, response, filterChain);
			});
		}
		verify(filterChain, times(0)).doFilter(eq(request), eq(response));
	}

	@Test
	public void testNormalExcluded() throws ServletException, IOException
	{
		FilterChain filterChain = mock(FilterChain.class);
		when(excludeFilter.isExcluded(eq("/path/"), eq(RequestMethod.GET))).thenReturn(true);
		HttpServletRequest request = new MockHttpServletRequest("GET", "/path/");
		HttpServletResponse response = new MockHttpServletResponse();
		try (MockedStatic<WFUtil> wfUtilMock = mockStatic(WFUtil.class))
		{
			wfUtilMock.when(WFUtil::isAuthenticated).thenReturn(false);
			authenticationFilter.doFilter(request, response, filterChain);
			wfUtilMock.verify(WFUtil::isAuthenticated, times(0));
		}
		verify(filterChain).doFilter(eq(request), eq(response));
	}

	@Test
	public void testNormalExcludedrm() throws ServletException, IOException
	{
		FilterChain filterChain = mock(FilterChain.class);
		excludeFilter.addExcluded("/path/", RequestMethod.POST);
		when(excludeFilter.isExcluded(eq("/path/"), eq(RequestMethod.POST))).thenReturn(true);
		when(excludeFilter.isExcluded(eq("/path/"), eq(RequestMethod.GET))).thenReturn(false);
		HttpServletRequest request = new MockHttpServletRequest("GET", "/path/");
		HttpServletResponse response = new MockHttpServletResponse();
		try (MockedStatic<WFUtil> wfUtilMock = mockStatic(WFUtil.class))
		{
			wfUtilMock.when(WFUtil::isAuthenticated).thenReturn(true);
			authenticationFilter.doFilter(request, response, filterChain);
			wfUtilMock.verify(WFUtil::isAuthenticated, times(1));
		}
		verify(filterChain).doFilter(eq(request), eq(response));
	}

	@Test
	public void testException1() throws ServletException, IOException
	{
		FilterChain filterChain = mock(FilterChain.class);
		when(excludeFilter.isExcluded(eq("/path/"), eq(RequestMethod.GET))).thenReturn(false);
		HttpServletRequest request = new MockHttpServletRequest("GET", "/path/");
		HttpServletResponse response = new MockHttpServletResponse();
		try (MockedStatic<WFUtil> wfUtilMock = mockStatic(WFUtil.class))
		{
			wfUtilMock.when(WFUtil::isAuthenticated).thenThrow(NullPointerException.class);
			Assertions.assertThrows(NullPointerException.class, () -> {
				authenticationFilter.doFilter(request, response, filterChain);
			});
		}
		verify(filterChain, times(0)).doFilter(eq(request), eq(response));
	}

	@Test
	public void testException2() throws ServletException, IOException
	{
		FilterChain filterChain = mock(FilterChain.class);
		when(excludeFilter.isExcluded(eq("/path/"), eq(RequestMethod.GET))).thenReturn(false);
		HttpServletRequest request = new MockHttpServletRequest("GET", "/path/")
		{
			public String getRequestURI()
			{
				throw new NullPointerException();
			};
		};
		HttpServletResponse response = new MockHttpServletResponse();
		try (MockedStatic<WFUtil> wfUtilMock = mockStatic(WFUtil.class))
		{
			wfUtilMock.when(WFUtil::isAuthenticated).thenReturn(true);
			Assertions.assertThrows(NullPointerException.class, () -> {
				authenticationFilter.doFilter(request, response, filterChain);
			});
		}
		verify(filterChain, times(0)).doFilter(eq(request), eq(response));
	}

}

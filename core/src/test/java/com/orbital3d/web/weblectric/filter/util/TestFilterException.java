package com.orbital3d.web.weblectric.filter.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.orbital3d.web.security.weblectricfence.exception.AuthenticationException;
import com.orbital3d.web.security.weblectricfence.exception.AuthorizationException;
import com.orbital3d.web.security.weblectricfence.filter.ExceptionFilter;
import com.orbital3d.web.security.weblectricfence.filter.util.FilterExceptionHandler;

class TestFilterException {
	private ExceptionFilter ef = new ExceptionFilter();
	private FilterChain fc = mock(FilterChain.class);

	@BeforeEach
	void init() {

	}

	@Test
	void testNoHandler1() throws IOException, ServletException {
		HttpServletRequest req = new MockHttpServletRequest();
		HttpServletResponse res = new MockHttpServletResponse();
		doThrow(NullPointerException.class).when(fc).doFilter(req, res);
		assertThrows(NullPointerException.class, () -> {
			ef.doFilter(req, res, fc);
		});
	}

	@Test
	void testNoHandler2() throws IOException, ServletException {
		HttpServletRequest req = new MockHttpServletRequest();
		HttpServletResponse res = new MockHttpServletResponse();
		doThrow(AuthenticationException.class).when(fc).doFilter(req, res);
		assertThrows(AuthenticationException.class, () -> {
			ef.doFilter(req, res, fc);
		});
	}

	@Test
	void testNoHandler3() throws IOException, ServletException {
		HttpServletRequest req = new MockHttpServletRequest();
		HttpServletResponse res = new MockHttpServletResponse();
		doThrow(AuthorizationException.class).when(fc).doFilter(req, res);
		assertThrows(AuthorizationException.class, () -> {
			ef.doFilter(req, res, fc);
		});
	}

	@Test
	void testExceptionHandlerInvoked1() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, IOException, ServletException {
		FilterExceptionHandler feh = mock(FilterExceptionHandler.class);
		AuthorizationException ae = mock(AuthorizationException.class);
		injectHandler(feh);
		HttpServletRequest req = new MockHttpServletRequest();
		HttpServletResponse res = new MockHttpServletResponse();
		doThrow(ae).when(fc).doFilter(req, res);
		assertDoesNotThrow(() -> {
			ef.doFilter(req, res, fc);
		});
		verify(feh).handleException(ae, req, res);
	}

	@Test
	void testExceptionHandlerInvoked2() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, IOException, ServletException {
		FilterExceptionHandler feh = mock(FilterExceptionHandler.class);
		AuthenticationException ae = mock(AuthenticationException.class);
		injectHandler(feh);
		HttpServletRequest req = new MockHttpServletRequest();
		HttpServletResponse res = new MockHttpServletResponse();
		doThrow(ae).when(fc).doFilter(req, res);
		assertDoesNotThrow(() -> {
			ef.doFilter(req, res, fc);
		});
		verify(feh).handleException(ae, req, res);
	}

	@Test
	void testExceptionHandlerInvoked3() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, IOException, ServletException {
		FilterExceptionHandler feh = mock(FilterExceptionHandler.class);
		NullPointerException ae = mock(NullPointerException.class);
		injectHandler(feh);
		HttpServletRequest req = new MockHttpServletRequest();
		HttpServletResponse res = new MockHttpServletResponse();
		doThrow(ae).when(fc).doFilter(req, res);
		assertDoesNotThrow(() -> {
			ef.doFilter(req, res, fc);
		});
		verify(feh).handleException(ae, req, res);
	}

	@Test
	void testExceptionInHandler() throws IOException, ServletException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		FilterExceptionHandler feh = mock(FilterExceptionHandler.class);
		NullPointerException ae = mock(NullPointerException.class);
		IllegalArgumentException ia = mock(IllegalArgumentException.class);
		injectHandler(feh);
		HttpServletRequest req = new MockHttpServletRequest();
		HttpServletResponse res = new MockHttpServletResponse();
		doThrow(ae).when(fc).doFilter(req, res);
		doThrow(ia).when(feh).handleException(ae, req, res);
		try {
			ef.doFilter(req, res, fc);
			fail("No exception");
		} catch (Exception e) {
			assertEquals(ia, e);
		}
	}

	@Test
	void testIOExceptionInHandler() throws IOException, ServletException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		FilterExceptionHandler feh = new FilterExceptionHandler() {
			@Override
			public void handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) {
				throw new RuntimeException(exception);
			}
		};
		// mock(FilterExceptionHandler.class);
		IOException ae = mock(IOException.class);
		IllegalArgumentException ia = mock(IllegalArgumentException.class);
		injectHandler(feh);
		HttpServletRequest req = new MockHttpServletRequest();
		HttpServletResponse res = new MockHttpServletResponse();
		doThrow(ae).when(fc).doFilter(req, res);
		try {
			ef.doFilter(req, res, fc);
			fail("No exception");
		} catch (Exception e) {
			assertEquals(ae, e.getCause());
		}
	}

	private void injectHandler(FilterExceptionHandler feh)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = ExceptionFilter.class.getDeclaredField("filterExceptionHadler");
		f.setAccessible(true);
		f.set(ef, feh);
	}
}

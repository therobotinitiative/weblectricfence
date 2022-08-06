package com.orbital3d.web.weblectric.authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.orbital3d.web.security.weblectricfence.authentication.AuthenticationToken;
import com.orbital3d.web.security.weblectricfence.authentication.AuthenticationWorker;
import com.orbital3d.web.security.weblectricfence.authentication.AuthenticationWorker.Authenticator;
import com.orbital3d.web.security.weblectricfence.authentication.AuthenticationWorkerImpl;
import com.orbital3d.web.security.weblectricfence.exception.AuthenticationException;
import com.orbital3d.web.security.weblectricfence.exception.UnsupportedAuthenticationTokenException;
import com.orbital3d.web.security.weblectricfence.type.UserIdentity;
import com.orbital3d.web.security.weblectricfence.util.FenceUtil;

@ExtendWith(MockitoExtension.class)
class TestAuthenticationWorker {
	private AuthenticationWorker authenticationWorker;
	@Mock
	private Authenticator authenticatorMock;

	@BeforeEach
	void init() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		authenticationWorker = new AuthenticationWorkerImpl();
		List<Authenticator> authenticators = new ArrayList<>();
		when(authenticatorMock.supports(any())).thenReturn(true);
		authenticators.add(authenticatorMock);
		Field field = AuthenticationWorkerImpl.class.getDeclaredField("authenticators");
		field.setAccessible(true);
		field.set(authenticationWorker, authenticators);
	}

	@Test
	void testOk() throws AuthenticationException, LoginException {
		try (MockedStatic<FenceUtil> util = Mockito.mockStatic(FenceUtil.class)) {
			// Mock Authenticator
			when(authenticatorMock.authenticate(any())).thenReturn(mock(UserIdentity.class));
			// Run test
			authenticationWorker.authenticate(mock(AuthenticationToken.class));
		}
	}

	@Test
	void testThrowsExceptionAfterReturningNull() throws AuthenticationException, LoginException {
		try (MockedStatic<FenceUtil> util = Mockito.mockStatic(FenceUtil.class)) {
			// Mock Authenticator
			when(authenticatorMock.authenticate(any())).thenReturn(null);
			// Run test
			Assertions.assertThrows(LoginException.class, () -> {
				authenticationWorker.authenticate(mock(AuthenticationToken.class));
			});
		}
	}

	@Test
	void testThrowsLoginException() throws AuthenticationException, LoginException {
		try (MockedStatic<FenceUtil> util = Mockito.mockStatic(FenceUtil.class)) {
			// Mock Authenticator
			when(authenticatorMock.authenticate(any())).thenThrow(LoginException.class);
			// Run test
			Assertions.assertThrows(LoginException.class, () -> {
				authenticationWorker.authenticate(mock(AuthenticationToken.class));
			});
		}
	}

	@Test
	void testThrowsException() throws AuthenticationException, LoginException {
		try (MockedStatic<FenceUtil> util = Mockito.mockStatic(FenceUtil.class)) {
			// Mock Authenticator
			when(authenticatorMock.authenticate(any())).thenThrow(IllegalArgumentException.class);
			// Run test
			Assertions.assertThrows(IllegalArgumentException.class, () -> {
				authenticationWorker.authenticate(mock(AuthenticationToken.class));
			});
		}
	}

	@Test
	void testThrowsNPException() throws AuthenticationException, LoginException {
		try (MockedStatic<FenceUtil> util = Mockito.mockStatic(FenceUtil.class)) {
			// Mock Authenticator
			when(authenticatorMock.authenticate(any())).thenThrow(NullPointerException.class);
			// Run test
			Assertions.assertThrows(NullPointerException.class, () -> {
				authenticationWorker.authenticate(mock(AuthenticationToken.class));
			});
		}
	}

	@Test
	void testNoSupportedAuthenticatorFound() {
		when(authenticatorMock.supports(any())).thenReturn(false);
		Assertions.assertThrows(UnsupportedAuthenticationTokenException.class, () -> {
			authenticationWorker.authenticate(mock(AuthenticationToken.class));
		});
	}
}

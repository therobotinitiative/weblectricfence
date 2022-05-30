package com.orbital3d.web.weblectric.authentication;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.orbital3d.web.security.weblectricfence.authentication.AuthenticationWorker;
import com.orbital3d.web.security.weblectricfence.authentication.AuthenticationWorker.Authenticator;
import com.orbital3d.web.security.weblectricfence.authentication.AuthenticationWorkerImpl;
import com.orbital3d.web.security.weblectricfence.exception.AuthenticationException;
import com.orbital3d.web.security.weblectricfence.type.UserIdentity;
import com.orbital3d.web.security.weblectricfence.util.WFUtil;

@ExtendWith(MockitoExtension.class)
public class TestAuthenticationWorker
{
	private AuthenticationWorker authenticationWorker;
	@Mock
	private Authenticator authenticatorMock;

	@BeforeEach
	public void init() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException
	{
		authenticationWorker = new AuthenticationWorkerImpl();
		Field field = AuthenticationWorkerImpl.class.getDeclaredField("authenticator");
		field.setAccessible(true);
		field.set(authenticationWorker, authenticatorMock);
	}

	@Test
	public void testOk() throws AuthenticationException
	{
		try (MockedStatic<WFUtil> util = Mockito.mockStatic(WFUtil.class))
		{
			// Mock Authenticator
			when(authenticatorMock.authenticate(anyString(), anyString())).thenReturn(mock(UserIdentity.class));
			// Run test
			authenticationWorker.authenticate("user", "pwd");
		}
	}

	@Test
	public void testThrowsExceptionAfterReturningNull() throws AuthenticationException
	{
		try (MockedStatic<WFUtil> util = Mockito.mockStatic(WFUtil.class))
		{
			// Mock Authenticator
			when(authenticatorMock.authenticate(anyString(), anyString())).thenReturn(null);
			// Run test
			Assertions.assertThrows(AuthenticationException.class, () -> {
				authenticationWorker.authenticate("user", "pwd");
			});
		}
	}

	@Test
	public void testThrowsException() throws AuthenticationException
	{
		try (MockedStatic<WFUtil> util = Mockito.mockStatic(WFUtil.class))
		{
			// Mock Authenticator
			when(authenticatorMock.authenticate(anyString(), anyString())).thenThrow(IllegalArgumentException.class);
			// Run test
			Assertions.assertThrows(IllegalArgumentException.class, () -> {
				authenticationWorker.authenticate("user", "pwd");
			});
		}
	}

	@Test
	public void testThrowsNPException() throws AuthenticationException
	{
		try (MockedStatic<WFUtil> util = Mockito.mockStatic(WFUtil.class))
		{
			// Mock Authenticator
			when(authenticatorMock.authenticate(anyString(), anyString())).thenThrow(NullPointerException.class);
			// Run test
			Assertions.assertThrows(NullPointerException.class, () -> {
				authenticationWorker.authenticate("user", "pwd");
			});
		}
	}
}

package com.orbital3d.web.weblectric.authorization;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.orbital3d.web.security.weblectricfence.authorization.AuthorizationWorker;
import com.orbital3d.web.security.weblectricfence.authorization.AuthorizationWorker.Authorizer;
import com.orbital3d.web.security.weblectricfence.authorization.AuthorizationWorkerImpl;
import com.orbital3d.web.security.weblectricfence.exception.AuthorizationException;
import com.orbital3d.web.security.weblectricfence.type.Permission;
import com.orbital3d.web.security.weblectricfence.type.WebLectricSubject;
import com.orbital3d.web.security.weblectricfence.util.WFUtil;

@ExtendWith(MockitoExtension.class)
public class TestAuthorizationWorker
{
	private AuthorizationWorker authorizerWorker;
	@Mock
	private Authorizer authorizer;

	@BeforeEach
	public void init() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		authorizerWorker = new AuthorizationWorkerImpl();
		Field field = AuthorizationWorkerImpl.class.getDeclaredField("authorizer");
		field.setAccessible(true);
		field.set(authorizerWorker, authorizer);
	}

	@Test
	public void testAuthorizeOk_donotgatherpermissions() throws AuthorizationException
	{
		WebLectricSubject subjectMock = mock(WebLectricSubject.class);
		final Permission permission = Permission.of("perm");
		try (MockedStatic<WFUtil> wfUtil = mockStatic(WFUtil.class))
		{
			wfUtil.when(WFUtil::getSubject).thenReturn(subjectMock);
			authorizerWorker.authorize(permission);
			verify(authorizer).authorize(eq(subjectMock), eq(permission));
			verify(authorizer, times(0)).gatherPermissions(any(WebLectricSubject.class));
		}
	}

	@Test
	public void testAuthorizeOk_firsttimepermissiongathering() throws AuthorizationException
	{
		WebLectricSubject subjectMock = mock(WebLectricSubject.class);
		when(subjectMock.getPermissions()).thenReturn(null);
		final Permission permission = Permission.of("perm");
		try (MockedStatic<WFUtil> wfUtil = mockStatic(WFUtil.class))
		{
			wfUtil.when(WFUtil::getSubject).thenReturn(subjectMock);
			authorizerWorker.authorize(permission);
			verify(authorizer).authorize(eq(subjectMock), eq(permission));
			verify(authorizer).gatherPermissions(eq(subjectMock));
		}
	}

	@Test
	public void testAuthorizeOk_firsttimepermissiongathering2() throws AuthorizationException
	{
		WebLectricSubject subjectMock = mock(WebLectricSubject.class);
		when(subjectMock.getPermissions()).thenReturn(null);
		final Permission permission = Permission.of("perm");
		try (MockedStatic<WFUtil> wfUtil = mockStatic(WFUtil.class))
		{
			wfUtil.when(WFUtil::getSubject).thenReturn(subjectMock);
			authorizerWorker.authorize(permission);
			when(subjectMock.getPermissions()).thenReturn(Collections.emptySet());
			authorizerWorker.authorize(permission);
			authorizerWorker.authorize(permission);
			verify(authorizer, times(3)).authorize(eq(subjectMock), eq(permission));
			verify(authorizer).gatherPermissions(eq(subjectMock));
		}
	}

	@Test
	public void testAuthorizeOk_permissionsgathered1() throws AuthorizationException
	{
		WebLectricSubject subjectMock = mock(WebLectricSubject.class);
		when(subjectMock.getPermissions()).thenReturn(null);
		final Permission permission = Permission.of("perm");
		final Set<Permission> permssions = Collections.emptySet();
		try (MockedStatic<WFUtil> wfUtil = mockStatic(WFUtil.class))
		{
			wfUtil.when(WFUtil::getSubject).thenReturn(subjectMock);
			when(subjectMock.getPermissions()).thenReturn(permssions);
			authorizerWorker.authorize(permission);
			verify(authorizer).authorize(eq(subjectMock), eq(permission));
			verify(authorizer, times(0)).gatherPermissions(eq(subjectMock));
		}
	}

	@Test
	public void testAuthorizeOk_permissionsgathered2() throws AuthorizationException
	{
		WebLectricSubject subjectMock = mock(WebLectricSubject.class);
		when(subjectMock.getPermissions()).thenReturn(null);
		final Permission permission = Permission.of("perm");
		final Set<Permission> permssions = Collections.emptySet();
		try (MockedStatic<WFUtil> wfUtil = mockStatic(WFUtil.class))
		{
			wfUtil.when(WFUtil::getSubject).thenReturn(subjectMock);
			when(subjectMock.getPermissions()).thenReturn(permssions);
			authorizerWorker.authorize(permission);
			verify(authorizer).authorize(eq(subjectMock), eq(permission));
			verify(authorizer, times(0)).gatherPermissions(eq(subjectMock));
			authorizerWorker.authorize(permission);
			verify(authorizer, times(2)).authorize(eq(subjectMock), eq(permission));
			verify(authorizer, times(0)).gatherPermissions(eq(subjectMock));
		}
	}

	@Test
	public void testAuthorizeOk_gatherpermissionsafterunauthorize() throws AuthorizationException
	{
		WebLectricSubject subjectMock = mock(WebLectricSubject.class);
		when(subjectMock.getPermissions()).thenReturn(null);
		final Permission permission = Permission.of("perm");
		final Set<Permission> permssions = Collections.emptySet();
		try (MockedStatic<WFUtil> wfUtil = mockStatic(WFUtil.class))
		{
			wfUtil.when(WFUtil::getSubject).thenReturn(subjectMock);
			when(subjectMock.getPermissions()).thenReturn(permssions);
			authorizerWorker.authorize(permission);
			verify(authorizer).authorize(eq(subjectMock), eq(permission));
			verify(authorizer, times(0)).gatherPermissions(eq(subjectMock));
			when(subjectMock.getPermissions()).thenReturn(null);
			authorizerWorker.unauthorize(subjectMock);
			authorizerWorker.authorize(permission);
			verify(authorizer, times(2)).authorize(eq(subjectMock), eq(permission));
			verify(authorizer, times(1)).gatherPermissions(eq(subjectMock));
			verify(subjectMock).setPermissions(isNull());
		}
	}

	@Test
	public void testUnauthorizeOk()
	{
		WebLectricSubject subjectMock = mock(WebLectricSubject.class);
		authorizerWorker.unauthorize(subjectMock);
		verify(subjectMock).setPermissions(isNull());
	}

	@Test
	public void testUnauthorizeMulripleTimes()
	{
		WebLectricSubject subjectMock = mock(WebLectricSubject.class);
		authorizerWorker.unauthorize(subjectMock);
		authorizerWorker.unauthorize(subjectMock);
		authorizerWorker.unauthorize(subjectMock);
		authorizerWorker.unauthorize(subjectMock);
		authorizerWorker.unauthorize(subjectMock);
		verify(subjectMock, times(5)).setPermissions(isNull());
	}
}

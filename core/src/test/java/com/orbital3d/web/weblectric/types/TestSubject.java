package com.orbital3d.web.weblectric.types;

import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.orbital3d.web.security.weblectricfence.type.Permission;
import com.orbital3d.web.security.weblectricfence.type.Subject;
import com.orbital3d.web.security.weblectricfence.type.UserIdentity;
import com.orbital3d.web.security.weblectricfence.type.WebLectricSubject;

public class TestSubject
{
	@Test
	public void testSFMOk()
	{
		Subject.of(mock(UserIdentity.class));
		Subject.of(null);
		Subject.of(mock(UserIdentity.class), Collections.emptySet());
		Subject.of(null, Collections.emptySet());
		Subject.of(mock(UserIdentity.class), null);
		Subject.of(null, null);
	}

	@Test
	public void testCannotModifyPermissionsAfterSet()
	{
		Subject s = Subject.of(mock(UserIdentity.class));
		Set<Permission> perms = new HashSet<>();
		perms.add(Permission.of("p1"));
		perms.add(Permission.of("p2"));
		perms.add(Permission.of("p3"));
		perms.add(Permission.of("p4"));
		s.setPermissions(perms);
		Assertions.assertEquals(4, s.getPermissions().size());
		perms.clear();
		Assertions.assertEquals(4, s.getPermissions().size());
	}

	@Test
	public void testCannotModifyPermissionsWithGet()
	{
		Subject s = Subject.of(mock(UserIdentity.class));
		Set<Permission> perms = new HashSet<>();
		perms.add(Permission.of("p1"));
		perms.add(Permission.of("p2"));
		perms.add(Permission.of("p3"));
		perms.add(Permission.of("p4"));
		s.setPermissions(perms);
		Assertions.assertEquals(4, s.getPermissions().size());
		Assertions.assertThrows(UnsupportedOperationException.class, () -> {
			s.getPermissions().clear();
		});
	}

	@Test
	public void testAssignelability()
	{
		Assertions.assertTrue(WebLectricSubject.class.isAssignableFrom(Subject.class));
	}
}

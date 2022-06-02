package com.orbital3d.web.weblectric.types;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.orbital3d.web.security.weblectricfence.type.Permission;
import com.orbital3d.web.security.weblectricfence.type.Subject;
import com.orbital3d.web.security.weblectricfence.type.WebLectricSubject;

public class TestSubject
{
	@Test
	public void testSFMOk()
	{
		Subject.of(new Object());
		Subject.of(null);
		Subject.of(new Object(), Collections.emptySet());
		Subject.of(null, Collections.emptySet());
		Subject.of(new Object(), null);
		Subject.of(null, null);
	}

	@Test
	public void testCannotModifyPermissionsAfterSet()
	{
		Subject s = Subject.of(new Object());
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
		Subject s = Subject.of(new Object());
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

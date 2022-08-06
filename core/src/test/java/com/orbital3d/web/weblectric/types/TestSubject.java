package com.orbital3d.web.weblectric.types;

import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.orbital3d.web.security.weblectricfence.type.Permission;
import com.orbital3d.web.security.weblectricfence.type.Subject;
import com.orbital3d.web.security.weblectricfence.type.UserIdentity;
import com.orbital3d.web.security.weblectricfence.type.WebLectricSubject;

class TestSubject {
	@Test
	void testSFMOk() {
		Subject.of(mock(UserIdentity.class));
		Subject.of(null);
		Subject.of(mock(UserIdentity.class), Collections.emptySet());
		Subject.of(null, Collections.emptySet());
		Subject.of(mock(UserIdentity.class), null);
		Subject.of(null, null);
	}

	@Test
	void testCannotModifyPermissionsAfterSet() {
		Subject s = Subject.of(mock(UserIdentity.class));
		Set<Permission> perms = new HashSet<>();
		perms.add(Permission.of("p1"));
		perms.add(Permission.of("p2"));
		perms.add(Permission.of("p3"));
		perms.add(Permission.of("p4"));
		s.setPermissions(perms);
		Assertions.assertEquals(4, IterableUtils.size(s.getPermissions()));
		perms.clear();
		Assertions.assertEquals(4, IterableUtils.size(s.getPermissions()));
	}

	@Test
	void testCannotModifyPermissionsWithGet() {
		Subject s = Subject.of(mock(UserIdentity.class));
		Set<Permission> perms = new HashSet<>();
		perms.add(Permission.of("p1"));
		perms.add(Permission.of("p2"));
		perms.add(Permission.of("p3"));
		perms.add(Permission.of("p4"));
		s.setPermissions(perms);
		Assertions.assertEquals(4, IterableUtils.size(s.getPermissions()));
		Assertions.assertThrows(UnsupportedOperationException.class, () -> {
			Iterator<Permission> iter = s.getPermissions().iterator();
			iter.next();
			iter.remove();
			s.getPermissions().iterator().next();
		});
	}

	@Test
	void testAssignelability() {
		Assertions.assertTrue(WebLectricSubject.class.isAssignableFrom(Subject.class));
	}
}

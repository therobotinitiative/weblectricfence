package com.orbital3d.web.weblectric.authorization;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMethod;

import com.orbital3d.web.security.weblectricfence.authorization.AuthorizationMatcher.EndPointContainer;
import com.orbital3d.web.security.weblectricfence.type.Permission;

class TestEndPointContainer {
	@Test
	void testSFMOk() {
		EndPointContainer.of("/", RequestMethod.DELETE, Permission.of("perm"));
	}

	@Test
	void testSFMException() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			EndPointContainer.of(null, RequestMethod.DELETE, Permission.of("perm"));
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			EndPointContainer.of("", RequestMethod.DELETE, Permission.of("perm"));
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			EndPointContainer.of(" ", RequestMethod.DELETE, Permission.of("perm"));
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			EndPointContainer.of("/", null, Permission.of("perm"));
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			EndPointContainer.of("/", RequestMethod.DELETE, null);
		});
	}

	@Test
	void testEquals() {
		final Permission p1 = Permission.of("perm");
		final Permission p2 = Permission.of("perm2");
		EndPointContainer epc1 = EndPointContainer.of("/app/[param}", RequestMethod.GET, p1);
		EndPointContainer epc2 = EndPointContainer.of("/app/[param}", RequestMethod.GET, p1);
		EndPointContainer epc3 = EndPointContainer.of("/app/", RequestMethod.GET, p1);
		EndPointContainer epc4 = EndPointContainer.of("/app/", RequestMethod.POST, p1);
		EndPointContainer epc5 = EndPointContainer.of("/app/", RequestMethod.POST, p2);
		Assertions.assertEquals(epc1, epc2);
		Assertions.assertNotEquals(epc1, epc3);
		Assertions.assertNotEquals(epc3, epc4);
		Assertions.assertNotEquals(epc4, epc5);
	}
}

package com.orbital3d.web.weblectric.authorization;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMethod;

import com.orbital3d.web.security.weblectricfence.authorization.AuthorizationMatcher;
import com.orbital3d.web.security.weblectricfence.authorization.AuthorizationMatcher.EndPointContainer;
import com.orbital3d.web.security.weblectricfence.authorization.DefaultAuthorizationMatcher;
import com.orbital3d.web.security.weblectricfence.type.Permission;

public class TestDefaultAuthorizationMatcher {
	private AuthorizationMatcher am;

	@BeforeEach
	public void init() {
		am = new DefaultAuthorizationMatcher();
	}

	@Test
	public void testAppendOk() {
		EndPointContainer epc1 = EndPointContainer.of("/path1", RequestMethod.GET, Permission.of("perm"));
		EndPointContainer epc2 = EndPointContainer.of("/path2", RequestMethod.GET, Permission.of("perm"));
		EndPointContainer epc3 = EndPointContainer.of("/path1", RequestMethod.GET, Permission.of("perm1"));
		am.append(epc1);
		am.append(epc2);
		am.append(epc3);
	}

	@Test
	public void testAppendMultipleTimesOk() {
		EndPointContainer epc1 = EndPointContainer.of("/path1", RequestMethod.GET, Permission.of("perm"));
		am.append(epc1);
		am.append(epc1);
		am.append(epc1);
	}

	@Test
	public void testAppendMultipleTimesPermissionFound() {
		final String path = "/path";
		Permission p = Permission.of("perm");
		EndPointContainer epc1 = EndPointContainer.of(path, RequestMethod.GET, p);
		am.append(epc1);
		am.append(epc1);
		am.append(epc1);
		Assertions.assertEquals(p, am.requiredPermission(path, RequestMethod.GET));
	}

	@Test
	public void testPathPermissionFound() {
		final String path = "/path";
		Permission p = Permission.of("perm1");
		EndPointContainer epc1 = EndPointContainer.of(path, RequestMethod.GET, p);
		EndPointContainer epc2 = EndPointContainer.of("/path2", RequestMethod.GET, Permission.of("perm"));
		EndPointContainer epc3 = EndPointContainer.of("/path1", RequestMethod.POST, Permission.of("perm1"));
		am.append(epc1);
		am.append(epc2);
		am.append(epc3);
		Assertions.assertEquals(p, am.requiredPermission(path, RequestMethod.GET));
	}

	@Test
	public void testPathNotFoundOnEmpty() {
		Assertions.assertNull(am.requiredPermission("/path", RequestMethod.GET));
	}

	@Test
	public void testPathPermissionRequestMethodNot() {
		final String path = "/path";
		Permission p1 = Permission.of("perm1");
		Permission p2 = Permission.of("perm1:perm2");
		Permission p3 = Permission.of("perm1:perm2:perm3");
		EndPointContainer epc1 = EndPointContainer.of(path, RequestMethod.GET, p1);
		EndPointContainer epc2 = EndPointContainer.of(path, RequestMethod.POST, p2);
		EndPointContainer epc3 = EndPointContainer.of(path, RequestMethod.PUT, p3);
		am.append(epc1);
		am.append(epc2);
		am.append(epc3);
		Assertions.assertEquals(p2, am.requiredPermission(path, RequestMethod.POST));
		Assertions.assertNull(am.requiredPermission(path, RequestMethod.DELETE));
	}

	@Test
	public void testIllegalArgument() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			am.append(null);
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			am.requiredPermission(null, RequestMethod.GET);
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			am.requiredPermission("  ", RequestMethod.GET);
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			am.requiredPermission("  ", null);
		});
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			am.requiredPermission("/", null);
		});
	}

	@Test
	public void testANTStyleParhMatching() {
		final String path1 = "/path/{param}";
		final String path1_param = "/path/parameter";
		final String path2 = "/path/";
		Permission p1 = Permission.of("perm1");
		Permission p2 = Permission.of("perm2");
		EndPointContainer epc1 = EndPointContainer.of(path1, RequestMethod.GET, p1);
		EndPointContainer epc2 = EndPointContainer.of(path2, RequestMethod.GET, p2);
		am.append(epc1);
		am.append(epc2);
		Assertions.assertEquals(p1, am.requiredPermission(path1_param, RequestMethod.GET));
		Assertions.assertNull(am.requiredPermission(path1_param, RequestMethod.POST));
		Assertions.assertEquals(p2, am.requiredPermission(path2, RequestMethod.GET));
	}

}

package com.orbital3d.web.weblectric.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.orbital3d.web.security.weblectricfence.type.Permission;

public class TestPermission {
	/**
	 * Convenience class.
	 * 
	 * @author msiren
	 *
	 */
	static class it {
		Permission p1;
		Permission p2;
		boolean expected;

		static it of(Permission p1, Permission p2, boolean expected) {
			it it = new it();
			it.p1 = p1;
			it.p2 = p2;
			it.expected = expected;
			return it;
		}

		static it of(String p1, String p2, boolean exp) {
			return it.of(Permission.of(p1), Permission.of(p2), exp);
		}
	}

	// Prepare permissions to test
	private static it[] t = {
			// True cases
			it.of("part1", "part1", true), it.of("part1:part2", "part1:part2", true),
			it.of("part1:part2:part3", "part1:part2:part3", true), it.of("part1:part2:part3", "part1:part2:*", true),
			it.of("part1:part2:part3", "part1:*:part3", true), it.of("part1:part2:part3", "*:part2:part3", true),
			// False cases
			it.of("part1", "no-perm", false), it.of("part1", "part1:notallowed", false),
			it.of("part1:part2:part3", "part1:part2:*", true), it.of("suer:duper:permission", "*", true),
			it.of("admin:user:add", "admin:user:*", true), it.of("admin:user:add", "admin:*", true),
			it.of("admin:user:add", "admin:user:remove", false), it.of("part1:notallowed", "part1", false),
			it.of("part1", "part1:notallowed", false), it.of("part1:part2:part3:part4", "part1:part2:*", true),
			it.of("part1:part2:part3:part4:part5", "part1:part2:*", true) };

	@Test
	public void testPermissionArray() {
		for (it t1 : t) {
			Assertions.assertEquals(t1.p1.isPermitted(t1.p2), t1.expected, "asserting " + t1.p1 + " with " + t1.p2);
		}
	}

	@Test
	public void testEquals() {
		Assertions.assertTrue(Permission.of("perm").equals(Permission.of("perm")));
		Assertions.assertFalse(Permission.of("perm").equals(Permission.of("diff-perm")));
		Assertions.assertTrue(Permission.of("perm:ission").equals(Permission.of("perm:ission")));
		Assertions.assertFalse(Permission.of("perm:oteherpart").equals(Permission.of("perm:*")));
	}
}

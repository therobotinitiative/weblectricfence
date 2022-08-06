package com.orbital3d.web.weblectric.util;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.orbital3d.web.security.weblectricfence.util.HashUtil;

class HashUtilTests {

	@Test
	void testAlgorithmAvailable() {
		try {
			HashUtil.fillSecure(new byte[1024]);
		} catch (NoSuchAlgorithmException e) {
			Assertions.fail("Algorithm not available");
		}
	}

	@Test
	void testDoNotGenerateSame() throws NoSuchAlgorithmException {
		List<byte[]> generated = new ArrayList<>();
		for (int i = 0; i < 1024; i++) {
			byte[] toAdd = HashUtil.fillSecure(new byte[1024]);
			if (generated.contains(toAdd)) {
				Assertions.fail("Sama array generated");
			}
			generated.add(toAdd);
		}
	}

	@Test
	void testNoSameToken() throws NoSuchAlgorithmException {
		List<byte[]> tokens = new ArrayList<>();
		for (int i = 0; i < 1024; i++) {
			byte[] token = HashUtil.generateToken();
			if (tokens.contains(token)) {
				Assertions.fail("Same token generated");
			}
			tokens.add(token);
		}
	}

	@Test
	void testNoSameShortToken() throws NoSuchAlgorithmException {
		List<byte[]> tokens = new ArrayList<>();
		for (int i = 0; i < 1024; i++) {
			byte[] token = HashUtil.generateShortToken();
			if (tokens.contains(token)) {
				Assertions.fail("Same short token generated");
			}
			tokens.add(token);
		}
	}
}

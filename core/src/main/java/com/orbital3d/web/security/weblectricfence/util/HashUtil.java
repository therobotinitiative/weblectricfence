package com.orbital3d.web.security.weblectricfence.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashUtil {
	private HashUtil() {
		// Only static methods
	}

	public static byte[] generateToken() throws NoSuchAlgorithmException {
		return HashUtil.fillSecure(new byte[2038]);
	}

	/**
	 * Generates token that is considered short.
	 * 
	 * @return Byte array containing the generated token
	 * @throws NoSuchAlgorithmException If hashing algorithm was not available
	 */
	public static byte[] generateShortToken() throws NoSuchAlgorithmException {
		return HashUtil.fillSecure(new byte[256]);
	}

	public static byte[] fillSecure(byte[] arrayToFill) throws NoSuchAlgorithmException {
		// Using .getInstancceStrong causes the system to halt in linux. Read
		// https://stackoverflow.com/questions/137212/how-to-deal-with-a-slow-securerandom-generator
		// for more
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.nextBytes(arrayToFill);
		return arrayToFill;
	}

	public static byte[] secureHash(byte[] arrayToHash) throws NoSuchAlgorithmException {
		return MessageDigest.getInstance("SHA-512").digest(arrayToHash);
	}
}

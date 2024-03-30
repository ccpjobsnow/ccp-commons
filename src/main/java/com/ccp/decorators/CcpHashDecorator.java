package com.ccp.decorators;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class CcpHashDecorator {
	public final String content;

	protected CcpHashDecorator(String content) {
		this.content = content;
	}

	public String toString() {
		return this.content;
	}

	
	public String asString(String algorithm) {
		BigInteger bi = this.asBigInteger(algorithm);

		String strHash = bi.toString(16).toLowerCase();

		return strHash;
	}
	
	private static HashMap<String, MessageDigest> messageDigests = new HashMap<>();
	static {
		try {
			messageDigests.put("MD5", MessageDigest.getInstance("MD5"));
			messageDigests.put("SHA1", MessageDigest.getInstance("SHA1"));
			messageDigests.put("SHA-256", MessageDigest.getInstance("SHA-256"));
			messageDigests.put("SHA-512", MessageDigest.getInstance("SHA-512"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	public BigInteger asBigInteger(String algorithm) {
		MessageDigest digest = messageDigests.get(algorithm);
		
		byte[] bytes = this.content.getBytes(StandardCharsets.UTF_8);
		byte[] hash = digest.digest(bytes);
		BigInteger bi = new BigInteger(hash);
		return bi;
	}


}

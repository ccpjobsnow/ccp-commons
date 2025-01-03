package com.ccp.decorators;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public enum CcpHashAlgorithm {
	MD5("MD5"),
	SHA1("SHA1"),
	SHA256("SHA-256"),
	SHA512("SHA-512")
	;
	private final String algorithm;

	private CcpHashAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	private static HashMap<CcpHashAlgorithm, MessageDigest> messageDigests = new HashMap<>();

	public 	MessageDigest getMessageDigest() {
		MessageDigest messageDigest = messageDigests.get(this);
		if(messageDigest == null) {
			MessageDigest instance;
			try {
				instance = MessageDigest.getInstance(this.algorithm);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("Algorithm '" + this.algorithm + "' not found", e);
			}
			messageDigests.put(this, instance);
			return instance;
		}
		return messageDigest;
	}
	
}

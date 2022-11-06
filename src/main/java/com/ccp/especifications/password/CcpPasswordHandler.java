package com.ccp.especifications.password;

public interface CcpPasswordHandler {

	boolean matches(String hash, String password);

	String getPasswordHash(String password);

}

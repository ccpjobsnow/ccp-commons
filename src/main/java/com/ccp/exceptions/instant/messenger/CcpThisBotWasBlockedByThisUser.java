package com.ccp.exceptions.instant.messenger;

@SuppressWarnings("serial")
public class CcpThisBotWasBlockedByThisUser extends RuntimeException {
	public final String token;

	public CcpThisBotWasBlockedByThisUser(String token) {
		this.token = token;
	}
	
}

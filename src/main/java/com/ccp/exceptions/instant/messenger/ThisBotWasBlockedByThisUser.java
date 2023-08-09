package com.ccp.exceptions.instant.messenger;

@SuppressWarnings("serial")
public class ThisBotWasBlockedByThisUser extends RuntimeException {
	public final String token;

	public ThisBotWasBlockedByThisUser(String token) {
		this.token = token;
	}
	
}

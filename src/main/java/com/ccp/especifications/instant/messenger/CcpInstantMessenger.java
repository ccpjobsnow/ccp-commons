package com.ccp.especifications.instant.messenger;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpInstantMessenger {

	
	Long getMembersCount(String botToken, Long chatId);
	
	Long sendMessage(String botToken, String message, Long chatId, Long replyTo);
	
	String getFileName(String botToken, CcpMapDecorator messageData);
	
	String extractTextFromMessage(String botToken, CcpMapDecorator messageData);
	
	@SuppressWarnings("serial")
	public static class InstantMessageApiIsUnavailable extends RuntimeException {}

	@SuppressWarnings("serial")
	public static class ThisBotWasBlockedByThisUser extends RuntimeException {}

	@SuppressWarnings("serial")
	public static class TooManyRequests extends RuntimeException {}


}

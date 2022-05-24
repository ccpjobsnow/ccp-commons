package com.ccp.especifications.instant.messenger;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpInstantMessenger {

	
	void sendMessageToSupport(String botToken, String message);
	
	Long getMembersCount(String botToken, Long chatId);
	
	void sendSlowlyMessage(String botToken, String message, Long chatId, Long replyTo);

	void sendMessage(String botToken, String message, Long chatId, Long replyTo);
	
	String getFileName(String botToken, CcpMapDecorator messageData);
	
	String extractTextFromMessage(String botToken, CcpMapDecorator messageData);
	
	
}

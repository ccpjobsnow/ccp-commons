package com.ccp.especifications.instant.messenger;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpInstantMessenger {

	
	void sendMessageToSupport(String message);
	
	Long getMembersCount(Long chatId);
	
	void sendSlowlyMessage(String message, Long chatId, Long replyTo);

	void sendMessage(String message, Long chatId, Long replyTo);
	
	String getFileName(CcpMapDecorator messageData);
	
	String extractTextFromMessage(CcpMapDecorator messageData);
	
	
}

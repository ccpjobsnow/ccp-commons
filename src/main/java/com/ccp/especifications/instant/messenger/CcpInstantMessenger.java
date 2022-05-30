package com.ccp.especifications.instant.messenger;

import java.util.function.Consumer;
import java.util.function.Predicate;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpInstantMessenger {

	
	void sendMessageToSupport(String botToken, String message);
	
	Long getMembersCount(String botToken, Long chatId);
	
	Long sendSlowlyMessage(String botToken, String message, Long chatId, Long replyTo, Predicate<String> isLockedUser, Consumer<String> lockUser);

	Long sendMessage(String botToken, String message, Long chatId, Long replyTo, Predicate<String> isLockedUser, Consumer<String> lockUser);
	
	String getFileName(String botToken, CcpMapDecorator messageData);
	
	String extractTextFromMessage(String botToken, CcpMapDecorator messageData);
	
	
}

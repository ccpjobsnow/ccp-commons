package com.ccp.especifications.email;

public interface CcpEmailSender {

	void send(String subject, String emailTo, String message, String format);
	
	default void send(String subject, String emailTo, String message) {
		this.send(subject, emailTo, message, "text/html");
	}

	void notifyFailure(Throwable e, String email);
}

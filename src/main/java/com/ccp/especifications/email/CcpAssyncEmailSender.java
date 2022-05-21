package com.ccp.especifications.email;

public class CcpAssyncEmailSender {
	private final CcpEmailSender sender;

	public CcpAssyncEmailSender(CcpEmailSender sender) {
		this.sender = sender;
	}

	public void send(String subject, String emailTo, String message, String format) {
		new Thread(() -> this.sender.send(subject, emailTo, message, format)).start();
	}

	public void send(String subject, String emailTo, String message) {
		new Thread(() -> this.sender.send(subject, emailTo, message)).start();
	}

	
	
}

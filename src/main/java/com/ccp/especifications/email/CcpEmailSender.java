package com.ccp.especifications.email;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpEmailSender {

	void send(CcpMapDecorator emailParameters) ;
	
	@SuppressWarnings("serial")
	public static class EmailWasNotSent extends RuntimeException {
		public final String errorDetails;

		public EmailWasNotSent(String errorDetails) {
			this.errorDetails = errorDetails;
		}
		
		
		
	}
	
}

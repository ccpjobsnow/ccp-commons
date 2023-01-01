package com.ccp.especifications.email;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.http.CcpHttpResponse;

public interface CcpEmailSender {

	void send(CcpMapDecorator emailParameters) ;
	
	@SuppressWarnings("serial")
	public static class ThereWasClientError extends RuntimeException {
		public final String message;

		public ThereWasClientError(CcpHttpResponse httpResponse) {
			this.message = httpResponse.httpResponse;
		}
	}
	
	@SuppressWarnings("serial")
	public static class EmailApiIsUnavailable extends RuntimeException{}
}

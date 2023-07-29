package com.ccp.exceptions.email;

import com.ccp.especifications.http.CcpHttpResponse;

@SuppressWarnings("serial")
public class ThereWasClientError extends RuntimeException {
	public final String message;

	public ThereWasClientError(CcpHttpResponse httpResponse) {
		this.message = httpResponse.httpResponse;
	}
}
package com.ccp.exceptions.email;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.http.CcpHttpResponse;

@SuppressWarnings("serial")
public class ThereWasClientError extends EmailError {
	
	public ThereWasClientError(CcpHttpResponse httpResponse, String apiUrl, String apiKey, CcpMapDecorator request, CcpMapDecorator headers) {
		super(httpResponse, apiUrl, apiKey, request, headers);
	}
	
}
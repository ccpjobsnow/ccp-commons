package com.ccp.exceptions.email;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.http.CcpHttpResponse;

@SuppressWarnings("serial")
public class EmailError extends RuntimeException {
	public final String response;
	public final String apiUrl;
	public final String apiKey;
	public final CcpMapDecorator request;
	public final CcpMapDecorator headers;
	public final Integer httpStatus;
	public final Long date;
	
	public EmailError(CcpHttpResponse httpResponse, String apiUrl, String apiKey, CcpMapDecorator request, CcpMapDecorator headers) {
		super(httpResponse.httpResponse);

		this.response = httpResponse.httpResponse;
		this.httpStatus = httpResponse.httpStatus;
		this.date = System.currentTimeMillis();
		this.apiUrl = apiUrl;
		this.apiKey = apiKey;
		this.request = request;
		this.headers = headers;
		
	}
	
	public CcpMapDecorator asEntity() {
		return new CcpMapDecorator((Object)this);
	}
	
}
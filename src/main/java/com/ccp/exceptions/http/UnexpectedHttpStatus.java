package com.ccp.exceptions.http;

import com.ccp.especifications.http.CcpHttpResponse;

@SuppressWarnings("serial")
public class UnexpectedHttpStatus extends RuntimeException{
	public final CcpHttpResponse response;

	public UnexpectedHttpStatus(CcpHttpResponse response) {
		this.response = response;
	}
	
	
	
}

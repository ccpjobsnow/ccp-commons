package com.ccp.exceptions.http;

import com.ccp.decorators.CcpMapDecorator;

@SuppressWarnings("serial")
public class CcpHttpClientError extends CcpHttpError {
	
	public CcpHttpClientError(CcpMapDecorator entity) {
		super(entity);
	}
	public CcpHttpClientError() {
		
	}
}
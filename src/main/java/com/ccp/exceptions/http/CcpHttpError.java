package com.ccp.exceptions.http;

import java.util.Set;

import com.ccp.decorators.CcpMapDecorator;

@SuppressWarnings("serial")
public class CcpHttpError extends RuntimeException {

	public final CcpMapDecorator entity;
	public final boolean clientError;
	public final boolean serverError;
	
	public CcpHttpError(String url, String method, CcpMapDecorator headers, String request, Integer status, String response, Set<String> expectedStatusList) {
		super("Details: " + getEntity(url, method, headers, request, status, response) + ". All expected status: " + expectedStatusList);
		this.entity = getEntity(url, method, headers, request, status, response);
		this.clientError = status >= 400 && status < 500;
		this.serverError = status >= 500 && status < 600;
	
	}
	private static CcpMapDecorator getEntity(String url, String method, CcpMapDecorator headers, String request, Integer status, String response) {
		return new CcpMapDecorator().put("url", url).put("method", method).put("headers", headers).put("request", request).put("status", status).put("response", response);
	}
	
	
}
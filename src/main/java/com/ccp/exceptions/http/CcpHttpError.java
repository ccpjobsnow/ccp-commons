package com.ccp.exceptions.http;

import java.util.Set;

import com.ccp.decorators.CcpMapDecorator;

@SuppressWarnings("serial")
public class CcpHttpError extends RuntimeException {

	public final CcpMapDecorator entity;

	public CcpHttpError(String url, String method, CcpMapDecorator headers, String request, Enum<?> apiType, Integer status, String response, Set<String> expectedStatusList) {
		super("Details: " + getEntity(url, method, headers, request, apiType, status, response) + ". All expected status: " + expectedStatusList);
	
		this.entity = getEntity(url, method, headers, request, apiType, status, response);
	}
	private static CcpMapDecorator getEntity(String url, String method, CcpMapDecorator headers, String request,
			Enum<?> apiType, Integer status, String response) {
		return new CcpMapDecorator().put("url", url).put("method", method).put("headers", headers).put("request", request).put("apiType", apiType.name()).put("status", status).put("response", response);
	}
	
	
}
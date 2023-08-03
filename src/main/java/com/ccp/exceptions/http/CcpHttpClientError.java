package com.ccp.exceptions.http;

import java.util.Set;

import com.ccp.decorators.CcpMapDecorator;

@SuppressWarnings("serial")	
public class CcpHttpClientError extends CcpHttpError {

	public CcpHttpClientError(String url, String method, CcpMapDecorator headers, String request, Enum<?> apiType,
			Integer status, String response, Set<String> expectedStatusList) {
		super(url, method, headers, request, apiType, status, response, expectedStatusList);
		// TODO Auto-generated constructor stub
	}


}
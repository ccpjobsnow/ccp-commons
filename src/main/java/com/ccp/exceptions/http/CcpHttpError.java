package com.ccp.exceptions.http;

import java.util.Set;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;

@SuppressWarnings("serial")
public class CcpHttpError extends RuntimeException {

	public final CcpJsonRepresentation entity;
	public final boolean clientError;
	public final boolean serverError;
	
	public CcpHttpError(String trace, String url, String method, CcpJsonRepresentation headers, String request, Integer status, String response, Set<String> expectedStatusList) {
		super("\n\n\nTrace: " + trace + "\nDetails: " + getEntity(url, method, headers, request, status, response) + "\n. All expected status: " + expectedStatusList);
		this.entity = getEntity(url, method, headers, request, status, response);
		this.clientError = status >= 400 && status < 500;
		this.serverError = status >= 500 && status < 600;
	
	}
	private static CcpJsonRepresentation getEntity(String url, String method, CcpJsonRepresentation headers, String request, Integer status, String response) {
		return CcpConstants.EMPTY_JSON.put("url", url).put("method", method).put("headers", headers).put("request", request).put("status", status).put("response", response);
	}
	
	
}
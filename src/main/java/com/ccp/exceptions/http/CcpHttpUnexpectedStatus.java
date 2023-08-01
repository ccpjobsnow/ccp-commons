package com.ccp.exceptions.http;

import com.ccp.especifications.http.CcpHttpResponse;

@SuppressWarnings("serial")
public class CcpHttpUnexpectedStatus extends RuntimeException{
	public final CcpHttpResponse response;

	public CcpHttpUnexpectedStatus(CcpHttpResponse response, String url, String method, String expectedStatusList) {
		super("The remote call ([" + method + "] " + url + ") returned an unexpected Http status(" + response.httpStatus + "). All expected status: " +expectedStatusList + ". Response details: " + response.toString());
		this.response = response;
	}
	
	
	
}

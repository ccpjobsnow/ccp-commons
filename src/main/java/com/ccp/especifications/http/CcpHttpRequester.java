package com.ccp.especifications.http;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpHttpRequester {
	CcpHttpResponse executeHttpRequest(String url, String method, CcpMapDecorator headers, String body);
}

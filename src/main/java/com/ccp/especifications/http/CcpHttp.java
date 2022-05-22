package com.ccp.especifications.http;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpHttp {
	CcpHttpResponse executeHttpRequest(String url, String method, CcpMapDecorator headers, String body);
}

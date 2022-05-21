package com.ccp.especifications.http;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpHttp {

	
	CcpMapDecorator executeHttpRequest(String url, String method, CcpMapDecorator headers, CcpMapDecorator body);
	
}

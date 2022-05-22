package com.ccp.especifications.db.utils;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpDbUtils {

	
	CcpMapDecorator executeHttpRequest(int expectedStatus, String url, String method, CcpMapDecorator headers, String body);

	CcpMapDecorator executeHttpRequest(String url, String method, CcpMapDecorator body, CcpMapDecorator flows);
	
	CcpMapDecorator executeHttpRequest(int expectedStatus, String[] resources, String complemento, String method, CcpMapDecorator body);

	CcpMapDecorator executeHttpRequest(CcpMapDecorator flows, String url, String method, CcpMapDecorator body);

	CcpMapDecorator executeHttpRequest(int expectedStatus, String url, String method);

}

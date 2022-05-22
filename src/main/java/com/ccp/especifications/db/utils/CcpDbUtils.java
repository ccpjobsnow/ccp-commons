package com.ccp.especifications.db.utils;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpDbUtils {

	CcpMapDecorator executeHttpRequest(String url, String method, int expectedStatus, CcpMapDecorator body, String[] resources);

	CcpMapDecorator executeHttpRequest(String url, String method,  int expectedStatus, String body, CcpMapDecorator headers);

	CcpMapDecorator executeHttpRequest(String url, String method, CcpMapDecorator flows, CcpMapDecorator body);

	CcpMapDecorator executeHttpRequest(String url, String method, int expectedStatus, CcpMapDecorator body);
}

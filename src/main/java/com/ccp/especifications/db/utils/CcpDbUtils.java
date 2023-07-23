package com.ccp.especifications.db.utils;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.http.CcpHttpResponseTransform;

public interface CcpDbUtils {

	<V> V executeHttpRequest(String url, String method, Integer expectedStatus, CcpMapDecorator body, String[] resources, CcpHttpResponseTransform<V> transformer);

	<V> V executeHttpRequest(String url, String method,  Integer expectedStatus, String body, CcpMapDecorator headers, CcpHttpResponseTransform<V> transformer);

	<V> V executeHttpRequest(String url, String method, CcpMapDecorator flows, CcpMapDecorator body, CcpHttpResponseTransform<V> transformer);

	<V> V executeHttpRequest(String url, String method, Integer expectedStatus, CcpMapDecorator body, CcpHttpResponseTransform<V> transformer);

}

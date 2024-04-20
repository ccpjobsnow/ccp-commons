package com.ccp.especifications.db.utils;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.http.CcpHttpResponseTransform;

public interface CcpDbRequester {

	<V> V executeHttpRequest(String trace, String url, String method, Integer expectedStatus, CcpJsonRepresentation body, String[] resources, CcpHttpResponseTransform<V> transformer);

	<V> V executeHttpRequest(String trace, String url, String method,  Integer expectedStatus, String body, CcpJsonRepresentation headers, CcpHttpResponseTransform<V> transformer);

	<V> V executeHttpRequest(String trace, String url, String method, CcpJsonRepresentation flows, CcpJsonRepresentation body, CcpHttpResponseTransform<V> transformer);

	<V> V executeHttpRequest(String trace, String url, String method, Integer expectedStatus, CcpJsonRepresentation body, CcpHttpResponseTransform<V> transformer);

	CcpJsonRepresentation getConnectionDetails();
}

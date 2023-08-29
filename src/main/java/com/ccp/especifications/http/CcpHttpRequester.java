package com.ccp.especifications.http;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpHttpRequester {
	CcpHttpResponse executeHttpRequest(String url, String method, CcpMapDecorator headers, String body);

	default CcpHttpResponse executeHttpRequest(String url, String method, CcpMapDecorator headers, String body, int expectedStatus) {
		CcpHttpResponse res = this.executeHttpRequest(url, method, headers, body);
		if(expectedStatus == res.httpStatus) {
			return res;
		}
		
		throw new RuntimeException(
				new CcpMapDecorator()
				.put("expectedStatus", expectedStatus)
				.put("realStatus", res.httpStatus)
				.put("response", res.httpResponse)
				.asPrettyJson());
	}
}

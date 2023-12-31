package com.ccp.especifications.http;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;

public interface CcpHttpRequester {
	CcpHttpResponse executeHttpRequest(String url, String method, CcpJsonRepresentation headers, String body);

	default CcpHttpResponse executeHttpRequest(String url, String method, CcpJsonRepresentation headers, String body, int expectedStatus) {
		CcpHttpResponse res = this.executeHttpRequest(url, method, headers, body);
		if(expectedStatus == res.httpStatus) {
			return res;
		}
		
		throw new RuntimeException(
				CcpConstants.EMPTY_JSON
				.put("expectedStatus", expectedStatus)
				.put("realStatus", res.httpStatus)
				.put("response", res.httpResponse)
				.asPrettyJson());
	}
}

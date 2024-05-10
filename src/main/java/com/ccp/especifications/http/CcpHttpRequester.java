package com.ccp.especifications.http;

import java.util.Arrays;
import java.util.List;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;

public interface CcpHttpRequester {
	CcpHttpResponse executeHttpRequest(String url, String method, CcpJsonRepresentation headers, String body);

	default CcpHttpResponse executeHttpRequest(String url, String method, CcpJsonRepresentation headers, String body, Integer... numbers) {
		CcpHttpResponse res = this.executeHttpRequest(url, method, headers, body);
		
		for (int expectedStatus : numbers) {
			if(expectedStatus == res.httpStatus) {
				return res;
			}
		}
		List<Integer> asList = Arrays.asList(numbers);
		throw new RuntimeException(
				CcpConstants.EMPTY_JSON
				.put("url", url)
				.put("method", method)
				.put("headers", headers)
				.put("body", body)
				.put("expectedStatus", asList)
				.put("realStatus", res.httpStatus)
				.put("response", res.httpResponse)
				.asPrettyJson());
	}
}

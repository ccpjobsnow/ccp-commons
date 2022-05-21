package com.ccp.especifications.http;

import java.util.Map;

import com.ccp.decorators.CcpMapDecorator;

public final class CcpHttpThrow {

	private final Map<Integer, RuntimeException> flows;
	
	private final CcpHttp ccpHttp;

	public CcpHttpThrow(Map<Integer, RuntimeException> flows, CcpHttp ccpHttp) {
		this.flows = flows;
		this.ccpHttp = ccpHttp;
	}
	
	
	public CcpMapDecorator executeHttpRequest(String url, String method, CcpMapDecorator headers, CcpMapDecorator body) {
		
		CcpMapDecorator _package = this.ccpHttp.executeHttpRequest(url, method, headers, body);
	
		int status = _package.getAsIntegerNumber("status");
		
		RuntimeException flow = this.flows.get(status);

		if(flow != null) {
			throw flow;
		}
		
		return _package.getSubMap("response");
	}
	
	
}

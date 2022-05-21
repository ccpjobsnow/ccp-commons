package com.ccp.especifications.http;

import java.util.Map;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpProcess;

public final class CcpHttpHandler {

	private final Map<Integer, CcpProcess> flows;
	
	private final CcpHttp ccpHttp;

	public CcpHttpHandler(Map<Integer, CcpProcess> flows, CcpHttp ccpHttp) {
		this.flows = flows;
		this.ccpHttp = ccpHttp;
	}
	
	
	public CcpMapDecorator executeHttpRequest() {
		
		CcpMapDecorator _package = this.ccpHttp.executeHttpRequest();
	
		int status = _package.getAsIntegerNumber("status");
		
		CcpProcess flow = this.flows.get(status);
		if(flow == null) {
			throw new RuntimeException("Sem tratamento para o http status: " + status);
		}
		CcpMapDecorator response = _package.getSubMap("response");
		CcpMapDecorator execute = flow.execute(response);
		return execute;
	}
	
	
}

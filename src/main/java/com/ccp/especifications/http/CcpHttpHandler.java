package com.ccp.especifications.http;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpProcess;

public final class CcpHttpHandler {

	private final CcpMapDecorator flows;
	
	private final CcpHttp ccpHttp;

	public CcpHttpHandler(CcpMapDecorator flows, CcpHttp ccpHttp) {
		this.flows = flows;
		this.ccpHttp = ccpHttp;
	}

	public CcpHttpHandler(Integer httpStatus, CcpHttp ccpHttp) {
		this.flows = new CcpMapDecorator().put("" + httpStatus, CcpConstants.doNothing);
		this.ccpHttp = ccpHttp;
	}
	
	
	public CcpMapDecorator executeHttpRequest(String url, String method, CcpMapDecorator headers, CcpMapDecorator body) {
		
		CcpMapDecorator _package = this.ccpHttp.executeHttpRequest(url, method, headers, body);
	
		int status = _package.getAsIntegerNumber("status");
		
		CcpProcess flow = this.flows.getAsObject("" + status);
		if(flow == null) {
			throw new RuntimeException("Sem tratamento para o http status: " + status);
		}
		CcpMapDecorator response = _package.getSubMap("response");
		CcpMapDecorator execute = flow.execute(response);
		return execute;
	}

	public CcpMapDecorator executeHttpRequest(String url, String method, CcpMapDecorator headers, String body) {
		
		CcpMapDecorator _package = this.ccpHttp.executeHttpRequest(url, method, headers, body);
	
		int status = _package.getAsIntegerNumber("status");
		
		CcpProcess flow = this.flows.getAsObject("" + status);
		if(flow == null) {
			throw new RuntimeException("Sem tratamento para o http status: " + status);
		}
		CcpMapDecorator response = _package.getSubMap("response");
		CcpMapDecorator execute = flow.execute(response);
		return execute;
	}
	
	
}

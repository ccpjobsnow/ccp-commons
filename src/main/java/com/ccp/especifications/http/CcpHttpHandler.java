package com.ccp.especifications.http;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpProcess;

public final class CcpHttpHandler {

	private final CcpMapDecorator flows;
	
	private final CcpHttpRequester ccpHttp;

	public CcpHttpHandler(CcpMapDecorator flows, CcpHttpRequester ccpHttp) {
		this.flows = flows;
		this.ccpHttp = ccpHttp;
	}

	public CcpHttpHandler(Integer httpStatus, CcpHttpRequester ccpHttp) {
		this.flows = new CcpMapDecorator().put("" + httpStatus, CcpConstants.doNothing);
		this.ccpHttp = ccpHttp;
	}
	
	
	public <V> V executeHttpRequest(String url, String method, CcpMapDecorator headers, CcpMapDecorator body, CcpHttpResponseTransform<V> transformer) {
		
		V executeHttpRequest = this.executeHttpRequest(url, method, headers, body.asJson(), transformer);
		return executeHttpRequest;
	}

	public <V>V executeHttpRequest(String url, String method, CcpMapDecorator headers, String body, CcpHttpResponseTransform<V> transformer) {
		
		CcpHttpResponse _package = this.ccpHttp.executeHttpRequest(url, method, headers, body);
	
		int status = _package.httpStatus;
		
		CcpProcess flow = this.flows.getAsObject("" + status);
		if(flow == null) {
			throw new RuntimeException("Status http não esperado: " + status);
		}
		
		V tranform = transformer.transform(_package);
		return tranform;
	}
	
	
}

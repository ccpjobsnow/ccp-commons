package com.ccp.especifications.http;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpMapDecorator;
import com.ccp.exceptions.http.CcpHttpUnexpectedStatus;
import com.ccp.process.CcpProcess;

public final class CcpHttpHandler {

	private final CcpMapDecorator flows;
	
	private final CcpHttpRequester ccpHttp;

	public CcpHttpHandler(CcpMapDecorator flows, CcpHttpRequester ccpHttp) {
		this.flows = flows;
		this.ccpHttp = ccpHttp;
	}

	public CcpHttpHandler(Integer httpStatus, CcpHttpRequester ccpHttp) {
		this.flows = new CcpMapDecorator().put(httpStatus.toString(), CcpConstants.DO_NOTHING);
		this.ccpHttp = ccpHttp;
	}
	
	public <V> V executeHttpSimplifiedGet(String url, CcpHttpResponseTransform<V> transformer) {
		V executeHttpRequest = this.executeHttpRequest(url, "GET", CcpConstants.EMPTY_JSON, CcpConstants.EMPTY_JSON, transformer);
		return executeHttpRequest;
	}
	
	public <V> V executeHttpRequest(String url, String method, CcpMapDecorator headers, CcpMapDecorator body, CcpHttpResponseTransform<V> transformer) {
		
		String asJson = body.asJson();
		V executeHttpRequest = this.executeHttpRequest(url, method, headers, asJson, transformer);
		return executeHttpRequest;
	}

	@SuppressWarnings("unchecked")
	public <V>V executeHttpRequest(String url, String method, CcpMapDecorator headers, String body, CcpHttpResponseTransform<V> transformer) {
		
		CcpHttpResponse response = this.ccpHttp.executeHttpRequest(url, method, headers, body);
	
		int status = response.httpStatus;
		
		CcpProcess flow = this.flows.getAsObject("" + status);
	
		if(flow == null) {
			throw new CcpHttpUnexpectedStatus(response, url, method, this.flows.keySet().toString());
		}
	
		boolean invalidSingleJson = response.isValidSingleJson() == false;
		
		V tranform = transformer.transform(response);

		if(invalidSingleJson) {
			return tranform;
		}
		
		if(tranform instanceof CcpMapDecorator == false) {
			return tranform;
		}

		CcpMapDecorator execute = flow.execute((CcpMapDecorator)tranform);
		return (V)execute;
		
	}
	
	
}

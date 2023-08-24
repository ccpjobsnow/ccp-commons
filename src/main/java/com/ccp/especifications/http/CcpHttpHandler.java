package com.ccp.especifications.http;

import java.util.function.Function;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpMapDecorator;
import com.ccp.dependency.injection.CcpInstanceInjection;
import com.ccp.exceptions.http.CcpHttpError;


public final class CcpHttpHandler {

	private final CcpMapDecorator flows;
	
	private final CcpHttpRequester ccpHttp = CcpInstanceInjection.getInstance(CcpHttpRequester.class);

	public CcpHttpHandler(CcpMapDecorator flows) {
		this.flows = flows;
	}

	public CcpHttpHandler(Integer httpStatus) {
		this.flows = new CcpMapDecorator().put(httpStatus.toString(), CcpConstants.DO_NOTHING);
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
	public <V>V executeHttpRequest(String url, String method, CcpMapDecorator headers, String request, CcpHttpResponseTransform<V> transformer) {
		
		CcpHttpResponse response = this.ccpHttp.executeHttpRequest(url, method, headers, request);
	
		int status = response.httpStatus;
		
		Function<CcpMapDecorator, CcpMapDecorator> flow = this.flows.getAsObject("" + status);
	
		if(flow == null) {
			throw new CcpHttpError(url, method, headers, request, status, response.httpResponse, this.flows.keySet());
		}
	
		boolean invalidSingleJson = response.isValidSingleJson() == false;
		
		V tranform = transformer.transform(response);

		if(invalidSingleJson) {
			return tranform;
		}
		
		if(tranform instanceof CcpMapDecorator == false) {
			return tranform;
		}

		CcpMapDecorator execute = flow.apply((CcpMapDecorator)tranform);
		return (V)execute;
		
	}
	
	
}

package com.ccp.especifications.http;

import java.util.function.Function;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpMapDecorator;
import com.ccp.exceptions.http.CcpHttpClientError;
import com.ccp.exceptions.http.CcpHttpError;
import com.ccp.exceptions.http.CcpHttpServerError;


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
	
	public <V> V executeHttpSimplifiedGet(String url, CcpHttpResponseTransform<V> transformer, Enum<?> apiType) {
		V executeHttpRequest = this.executeHttpRequest(url, "GET", CcpConstants.EMPTY_JSON, CcpConstants.EMPTY_JSON, transformer, apiType);
		return executeHttpRequest;
	}
	
	public <V> V executeHttpRequest(String url, String method, CcpMapDecorator headers, CcpMapDecorator body, CcpHttpResponseTransform<V> transformer, Enum<?> apiType) {
		
		String asJson = body.asJson();
		V executeHttpRequest = this.executeHttpRequest(url, method, headers, asJson, transformer, apiType);
		return executeHttpRequest;
	}

	@SuppressWarnings("unchecked")
	public <V>V executeHttpRequest(String url, String method, CcpMapDecorator headers, String request, CcpHttpResponseTransform<V> transformer, Enum<?> apiType) {
		
		CcpHttpResponse response = this.ccpHttp.executeHttpRequest(url, method, headers, request);
	
		int status = response.httpStatus;
		
		Function<CcpMapDecorator, CcpMapDecorator> flow = this.flows.getAsObject("" + status);
	
		if(flow == null) {
			if(response.httpStatus >= 400 && response.httpStatus < 500) {
				throw new CcpHttpClientError(url, method, headers, request, apiType, status, response.httpResponse, this.flows.keySet());
			}

			if(response.httpStatus >= 500 && response.httpStatus < 600) {
				throw new CcpHttpServerError(url, method, headers, request, apiType, status, response.httpResponse, this.flows.keySet());
			}
			
			throw new CcpHttpError(url, method, headers, request, apiType, status, response.httpResponse, this.flows.keySet());
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

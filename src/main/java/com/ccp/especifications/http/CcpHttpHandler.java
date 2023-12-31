package com.ccp.especifications.http;

import java.util.function.Function;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.exceptions.http.CcpHttpError;


public final class CcpHttpHandler {

	private final CcpJsonRepresentation flows;
	
	private final CcpHttpRequester ccpHttp = CcpDependencyInjection.getDependency(CcpHttpRequester.class);

	public CcpHttpHandler(CcpJsonRepresentation flows) {
		this.flows = flows;
	}

	public CcpHttpHandler(Integer httpStatus) {
		this.flows = CcpConstants.EMPTY_JSON.put(httpStatus.toString(), CcpConstants.DO_NOTHING);
	}
	
	public <V> V executeHttpSimplifiedGet(String url, CcpHttpResponseTransform<V> transformer) {
		V executeHttpRequest = this.executeHttpRequest(url, "GET", CcpConstants.EMPTY_JSON, CcpConstants.EMPTY_JSON, transformer);
		return executeHttpRequest;
	}
	
	public <V> V executeHttpRequest(String url, String method, CcpJsonRepresentation headers, CcpJsonRepresentation body, CcpHttpResponseTransform<V> transformer) {
		
		String asJson = body.asUgglyJson();
		V executeHttpRequest = this.executeHttpRequest(url, method, headers, asJson, transformer);
		return executeHttpRequest;
	}

	@SuppressWarnings("unchecked")
	public <V>V executeHttpRequest(String url, String method, CcpJsonRepresentation headers, String request, CcpHttpResponseTransform<V> transformer) {
		
		CcpHttpResponse response = this.ccpHttp.executeHttpRequest(url, method, headers, request);
	
		int status = response.httpStatus;
		
		Function<CcpJsonRepresentation, CcpJsonRepresentation> flow = this.flows.getAsObject("" + status);
	
		if(flow == null) {
			throw new CcpHttpError(url, method, headers, request, status, response.httpResponse, this.flows.keySet());
		}
	
		boolean invalidSingleJson = response.isValidSingleJson() == false;
		
		V tranform = transformer.transform(response);

		if(invalidSingleJson) {
			return tranform;
		}
		
		if(tranform instanceof CcpJsonRepresentation == false) {
			return tranform;
		}

		CcpJsonRepresentation execute = flow.apply((CcpJsonRepresentation)tranform);
		return (V)execute;
		
	}
	
	
}

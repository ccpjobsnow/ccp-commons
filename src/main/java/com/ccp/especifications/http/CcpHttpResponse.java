package com.ccp.especifications.http;

import java.util.Base64;
import java.util.List;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpMapDecorator;
import com.ccp.decorators.CcpStringDecorator;

public class CcpHttpResponse {
	
	public final String httpResponse;
	public final int httpStatus;
	
	
	public CcpHttpResponse(String httpResponse, int httpStatus) {
		this.httpResponse = httpResponse;
		this.httpStatus = httpStatus;
	}

	public CcpMapDecorator asSingleRecord() {
		return new CcpStringDecorator(this.httpResponse).map();
	}
	
	@SuppressWarnings("unchecked")
	public List<CcpMapDecorator> asListRecord(){
		try {
			List<CcpMapDecorator> fromJson = CcpConstants.gson.fromJson(this.httpResponse, List.class);
			return fromJson; 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object> asListObject(){
		try {
			List<Object> fromJson = CcpConstants.gson.fromJson(this.httpResponse, List.class);
			return fromJson; 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String asBase64() {
		String encodeToString = Base64.getEncoder().encodeToString(this.httpResponse.getBytes());
		
		return encodeToString;
	}
}

package com.ccp.especifications.http;

import java.util.Base64;
import java.util.List;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.decorators.CcpTextDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.json.CcpJsonHandler;

public class CcpHttpResponse {

	public final String httpResponse;
	final int httpStatus;
	
	
	public CcpHttpResponse(String httpResponse, int httpStatus) {
		this.httpResponse = httpResponse;
		this.httpStatus = httpStatus;
	}
	
	public boolean isValidSingleJson() {
		if(this.httpResponse.trim().isEmpty()) {
			return true;
		}
		CcpStringDecorator ccpStringDecorator = new CcpStringDecorator(this.httpResponse);
		CcpTextDecorator text = ccpStringDecorator.text();
		boolean validSingleJson = text.isValidSingleJson();
		return validSingleJson;
	}
	
	public CcpMapDecorator asSingleJson() {
		try {
			return new CcpStringDecorator(this.httpResponse).map();
		} catch (Exception e) {
			return new CcpMapDecorator();
		}
	}
	
	public List<CcpMapDecorator> asListRecord(){
		try {
			CcpJsonHandler json = CcpDependencyInjection.getDependency(CcpJsonHandler.class);
			List<CcpMapDecorator> fromJson = json.fromJson(this.httpResponse);
			return fromJson; 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<Object> asListObject(){
		try {
			CcpJsonHandler json = CcpDependencyInjection.getDependency(CcpJsonHandler.class);
			List<Object> fromJson = json.fromJson(this.httpResponse);
			return fromJson; 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String asBase64() {
		String encodeToString = Base64.getEncoder().encodeToString(this.httpResponse.getBytes());
		
		return encodeToString;
	}
	
	@Override
	public String toString() {
		return new CcpMapDecorator()
				.put("httpStatus", this.httpStatus)
				.put("httpResponse", this.httpResponse)
				.toString();
	}
	

	private boolean isInRange(int range) {
		if(this.httpStatus < range) {
			return false;
		}
		if(this.httpStatus > (range + 99)) {
			return false;
		}
		return true;
		
	}
	
	public boolean isClientError() {
		return this.isInRange(400);
	}
	
	public boolean isServerError() {
		return this.isInRange(500);
	}

	public boolean isSuccess() {
		return this.isInRange(200);
	}
}

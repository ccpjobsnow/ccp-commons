package com.ccp.exceptions.commons;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpStepResult;

@SuppressWarnings("serial")
public class CcpFlow extends RuntimeException{
	
	public final CcpMapDecorator values;
	
	public final int status;

	public CcpFlow(CcpMapDecorator values, int status, String message, Throwable cause) {
		super(message, cause);
		this.values = values;
		this.status = status;
	}
	
	public CcpFlow (CcpStepResult stepResult) {
		this.values = stepResult.getData();
		this.status = stepResult.status;

	}
	public CcpFlow(CcpMapDecorator values, int status) {
		this.values = values;
		this.status = status;
	}

	public CcpFlow(CcpMapDecorator values, Integer status, String message) {
		super(message.trim().isEmpty() ? new CcpMapDecorator().put("values", values).put("status", status).asJson():message);
		this.values = values;
		this.status = status;
	}
	
	
}

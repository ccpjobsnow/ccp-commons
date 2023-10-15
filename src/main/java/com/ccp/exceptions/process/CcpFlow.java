package com.ccp.exceptions.process;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpStepResult;

@SuppressWarnings("serial")
public class CcpFlow extends RuntimeException{
	
	public final CcpMapDecorator values;
	
	public final int status;

	public CcpFlow (CcpStepResult stepResult) {
		this.values = stepResult.getData();
		this.status = stepResult.status;

	}
	public CcpFlow(CcpMapDecorator values, int status) {
		super(values.put("status", status).asPrettyJson());
		this.values = values;
		this.status = status;
	}

	public CcpFlow(CcpMapDecorator values, Integer status, String message) {
		super(message);
		this.values = values;
		this.status = status;
	}
	
	
}

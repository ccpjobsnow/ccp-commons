package com.ccp.exceptions.commons;

import com.ccp.decorators.CcpMapDecorator;

@SuppressWarnings("serial")
public class Flow extends RuntimeException{
	
	public final CcpMapDecorator values;
	
	public final int status;

	public Flow(CcpMapDecorator values, int status, String message, Throwable cause) {
		super(message, cause);
		this.values = values;
		this.status = status;
	}
	
	public Flow(CcpMapDecorator values, int status) {
		this.values = values;
		this.status = status;
	}

	public Flow(CcpMapDecorator values, Integer status, String message) {
		super(message);
		this.values = values;
		this.status = status;
	}
	
	
}

package com.ccp.exceptions.process;

import com.ccp.decorators.CcpJsonRepresentation;

@SuppressWarnings("serial")
public class CcpFlow extends RuntimeException{
	
	public final CcpJsonRepresentation values;
	
	public final int status;
	
	public final String[] fields;

	public CcpFlow(CcpJsonRepresentation values, int status, String... fields) {
		super(values.put("status", status).asPrettyJson());
		this.values = values;
		this.status = status;
		this.fields = fields;
	}

	public CcpFlow(CcpJsonRepresentation values, Integer status, String message, String... fields) {
		super(message);
		this.values = values;
		this.status = status;
		this.fields = fields;
	}
	
	
}

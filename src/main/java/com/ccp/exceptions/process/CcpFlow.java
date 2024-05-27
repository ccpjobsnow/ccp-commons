package com.ccp.exceptions.process;

import com.ccp.decorators.CcpJsonRepresentation;

@SuppressWarnings("serial")
public class CcpFlow extends RuntimeException{
	
	public final CcpJsonRepresentation json;
	
	public final int status;
	
	public final String[] fields;

	public CcpFlow(CcpJsonRepresentation json, int status, String... fields) {
		super(json.put("status", status).asPrettyJson());
		this.json = json;
		this.status = status;
		this.fields = fields;
	}

	public CcpFlow(CcpJsonRepresentation json, Integer status, String message, String... fields) {
		super(message);
		this.json = json;
		this.status = status;
		this.fields = fields;
	}
	
	
}

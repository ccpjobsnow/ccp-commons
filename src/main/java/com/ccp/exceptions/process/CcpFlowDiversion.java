package com.ccp.exceptions.process;

import com.ccp.constantes.CcpOtherConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.process.CcpProcessStatus;

@SuppressWarnings("serial")
public class CcpFlowDiversion extends RuntimeException{
	
	public final CcpJsonRepresentation json;
	
	public final CcpProcessStatus status;
	
	public final String[] fields;

	public CcpFlowDiversion(CcpProcessStatus status, String... fields) {
		this(CcpOtherConstants.EMPTY_JSON, status, fields);
	}

	public CcpFlowDiversion(CcpJsonRepresentation json, CcpProcessStatus status, String... fields) {
		super(json.put("statusNumber", status.asNumber()).put("statusName", status.name()).asPrettyJson());
		this.json = json;
		this.status = status;
		this.fields = fields;
	}

	public CcpFlowDiversion(CcpJsonRepresentation json, CcpProcessStatus status, String message, String... fields) {
		super(message);
		this.json = json;
		this.status = status;
		this.fields = fields;
	}
	
	
}

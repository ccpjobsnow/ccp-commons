package com.ccp.validation;

import com.ccp.decorators.CcpJsonRepresentation;

public class CcpJsonValidation {

	public final CcpJsonRepresentation content;
	public final String[] fields;

	public CcpJsonValidation(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}

	public CcpJsonFieldsRangeValidation withRange() {
		return new CcpJsonFieldsRangeValidation(this.content, this.fields);
	}
	
	public CcpJsonFieldsRestrictedValidation withAllowed() {
		return new CcpJsonFieldsRestrictedValidation(this.content, this.fields);
	}
	
	public CcpJsonFieldsTypeValidation thatTheTypes() {
		return new CcpJsonFieldsTypeValidation(this.content, this.fields);
	}

	public CcpJsonFieldsArrayValidation asArrays(){
		return new CcpJsonFieldsArrayValidation(content, this.fields);
	}
}

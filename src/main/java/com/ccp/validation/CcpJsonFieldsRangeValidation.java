package com.ccp.validation;

import com.ccp.decorators.CcpJsonRepresentation;

public class CcpJsonFieldsRangeValidation {

	public final CcpJsonRepresentation content;
	public final String[] fields;

	

	protected CcpJsonFieldsRangeValidation(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}

	public CcpJsonFieldsRangeArrayValidation fromArrays() {
		return new CcpJsonFieldsRangeArrayValidation(this.content, this.fields);
	}

	public CcpJsonFieldsRangeObjectValidation fromObjects() {
		return new CcpJsonFieldsRangeObjectValidation(this.content, this.fields);
	}
}

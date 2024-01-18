package com.ccp.validation;

import com.ccp.decorators.CcpJsonRepresentation;

public class CcpJsonFieldsRestrictedValidation {

	public final CcpJsonRepresentation content;
	public final String[] fields;

	
	
	protected CcpJsonFieldsRestrictedValidation(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}

	public CcpJsonFieldsRestrictedNumbersValidation numbers() {
		return new CcpJsonFieldsRestrictedNumbersValidation(this.content, this.fields);
	}

	public CcpJsonFieldsRestrictedStringsValidation strings() {
		return new CcpJsonFieldsRestrictedStringsValidation(this.content, this.fields);
	}

}

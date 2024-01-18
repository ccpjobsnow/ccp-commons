package com.ccp.validation;

import com.ccp.decorators.CcpJsonRepresentation;

public class CcpJsonFieldsRangeObjectValidation {

	public final CcpJsonRepresentation content;
	public final String[] fields;

	
	protected CcpJsonFieldsRangeObjectValidation(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}

	public CcpJsonFieldsRangeObjectNumberValidation asNumberThat() {
		return new CcpJsonFieldsRangeObjectNumberValidation(this.content, this.fields);
	}
	
	public CcpJsonFieldsRangeObjectStringValidation asStringWithLenght() {
		return new CcpJsonFieldsRangeObjectStringValidation(this.content, this.fields);
	}
	
	
}

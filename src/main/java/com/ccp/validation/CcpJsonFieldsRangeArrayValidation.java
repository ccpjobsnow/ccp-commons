package com.ccp.validation;

import com.ccp.decorators.CcpJsonRepresentation;

public class CcpJsonFieldsRangeArrayValidation {

	public final CcpJsonRepresentation content;
	public final String[] fields;

	
	protected CcpJsonFieldsRangeArrayValidation(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}

	public CcpJsonFieldsRangeArrayStringLengthValidation withStringLength() {
		return new CcpJsonFieldsRangeArrayStringLengthValidation(this.content, this.fields);
	}
	
	public CcpJsonFieldsRangeArraySizeValidation withSize() {
		return new CcpJsonFieldsRangeArraySizeValidation(this.content, this.fields);
	}
	
	public CcpJsonFieldsRangeArrayNumbersValidation withNumbers() {
		return new CcpJsonFieldsRangeArrayNumbersValidation(this.content, this.fields);
	}
	
}

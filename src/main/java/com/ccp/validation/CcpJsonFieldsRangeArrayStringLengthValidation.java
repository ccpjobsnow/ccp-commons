package com.ccp.validation;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpJsonRepresentation.ValueExtractor;

public class CcpJsonFieldsRangeArrayStringLengthValidation {

	public final CcpJsonRepresentation content;
	public final String[] fields;

	protected CcpJsonFieldsRangeArrayStringLengthValidation(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}

	//json.validate().range().array().ofString()
	
	public boolean allItemsHasLengthGreaterThan(int limit) {
		boolean test = CcpJsonFieldsArrayValidation.test((d, x) -> d.greaterThan(x), this.valueExtractor, this.content, limit, this.fields);
		return test;
	}

	public boolean allItemsHasLengthGreaterOrEqualsThan(int limit) {
		boolean test = CcpJsonFieldsArrayValidation.test((d, x) -> d.greaterOrEqualsTo(x), this.valueExtractor, this.content, limit, this.fields);
		return test;
	}
	public boolean allItemsHasLengthLessThan(int limit) {
		boolean test = CcpJsonFieldsArrayValidation.test((d, x) -> d.lessThan(x), this.valueExtractor, this.content, limit, this.fields);
		return test;
	}

	public boolean allItemsHasLengthLessOrEqualsThan(int limit) {
		boolean test = CcpJsonFieldsArrayValidation.test((d, x) -> d.lessOrEqualsTo(x), this.valueExtractor, this.content, limit, this.fields);
		return test;
	}

	public boolean allItemsHasLengthEqualsTo(int limit) {
		boolean test = CcpJsonFieldsArrayValidation.test((d, x) -> d.equalsTo(x), this.valueExtractor, this.content, limit, this.fields);
		return test;
	}
	ValueExtractor<String> valueExtractor = (x, json) -> "" + json.getAsString(x).length();

	
}

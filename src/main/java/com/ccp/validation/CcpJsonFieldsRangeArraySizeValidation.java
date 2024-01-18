package com.ccp.validation;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpJsonRepresentation.ValueExtractor;

public class CcpJsonFieldsRangeArraySizeValidation {

	public final CcpJsonRepresentation content;
	
	public final String[] fields;


	protected CcpJsonFieldsRangeArraySizeValidation(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}

	//json.validate().range().array().size()
	
	public boolean isGreaterThan(int limit) {
		boolean test = CcpJsonFieldsArrayValidation.test((d, x) -> d.greaterThan(x), this.valueExtractor, this.content, limit, this.fields);
		return test;
	}

	public boolean isGreaterOrEqualsThan(int limit) {
		boolean test = CcpJsonFieldsArrayValidation.test((d, x) -> d.greaterOrEqualsTo(x), this.valueExtractor, this.content, limit, this.fields);
		return test;
	}
	public boolean isLessThan(int limit) {
		boolean test = CcpJsonFieldsArrayValidation.test((d, x) -> d.lessThan(x), this.valueExtractor, this.content, limit, this.fields);
		return test;
	}

	public boolean isLessOrEqualsThan(int limit) {
		boolean test = CcpJsonFieldsArrayValidation.test((d, x) -> d.lessOrEqualsTo(x), this.valueExtractor, this.content, limit, this.fields);
		return test;
	}

	public boolean isEqualsTo(int limit) {
		boolean test = CcpJsonFieldsArrayValidation.test((d, x) -> d.equalsTo(x), this.valueExtractor, this.content, limit, this.fields);
		return test;
	}
	ValueExtractor<String> valueExtractor = (x, json) -> "" + json.getAsObjectList(x).size();

	
	
	
}

package com.ccp.validation;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpJsonRepresentation.ValueExtractor;

public class CcpJsonFieldsRangeArrayNumbersValidation {

	public final CcpJsonRepresentation content;
	
	public final String[] fields;
	
	protected CcpJsonFieldsRangeArrayNumbersValidation(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}

//json.validate().range().array().ofNumbers()
	
	public boolean allItemsAreGreaterThan(int limit) {
		boolean test = CcpJsonFieldsArrayValidation.test((d, x) -> d.greaterThan(x), this.valueExtractor, this.content, limit, this.fields);
		return test;
	}

	public boolean allItemsAreGreaterOrEqualsThan(int limit) {
		boolean test = CcpJsonFieldsArrayValidation.test((d, x) -> d.greaterOrEqualsTo(x), this.valueExtractor, this.content, limit, this.fields);
		return test;
	}
	public boolean allItemsAreLessThan(int limit) {
		boolean test = CcpJsonFieldsArrayValidation.test((d, x) -> d.lessThan(x), this.valueExtractor, this.content, limit, this.fields);
		return test;
	}

	public boolean allItemsAreLessOrEqualsThan(int limit) {
		boolean test = CcpJsonFieldsArrayValidation.test((d, x) -> d.lessOrEqualsTo(x), this.valueExtractor, this.content, limit, this.fields);
		return test;
	}

	public boolean allItemsAreEqualsTo(int limit) {
		boolean test = CcpJsonFieldsArrayValidation.test((d, x) -> d.equalsTo(x), this.valueExtractor, this.content, limit, this.fields);
		return test;
	}
	ValueExtractor<String> valueExtractor = (x, json) -> json.getAsString(x);
}
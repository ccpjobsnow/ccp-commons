package com.ccp.validation;

import com.ccp.decorators.CcpJsonRepresentation;

public class CcpJsonFieldsRangeObjectStringValidation {

	public final CcpJsonRepresentation content;

	public final String[] fields;


	protected CcpJsonFieldsRangeObjectStringValidation(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}
	//json.validate().range().object().asString()

	
	public boolean isGreaterThan(int limit) {

		for (String field : this.fields) {
			Long number = this.content.getAsLongNumber(field);
			if(number < limit) {
				return false;
			}
		}
		return true;
	}
	public boolean isGreaterOrEqualsTo(int limit) {

		for (String field : this.fields) {
			Long number = this.content.getAsLongNumber(field);
			if(number <= limit) {
				return false;
			}
		}
		return true;
	}
	public boolean isLessThan(int limit) {

		for (String field : this.fields) {
			Long number = this.content.getAsLongNumber(field);
			if(number > limit) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isLessOrEqualsTo(int limit) {

		for (String field : this.fields) {
			Long number = this.content.getAsLongNumber(field);
			if(number >= limit) {
				return false;
			}
		}
		return true;
	}

	public boolean isEqualsTo(int limit) {

		for (String field : this.fields) {
			Long number = this.content.getAsLongNumber(field);
			if(number != limit) {
				return false;
			}
		}
		return true;
	}

}

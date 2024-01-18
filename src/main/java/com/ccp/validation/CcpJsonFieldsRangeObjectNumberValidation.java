package com.ccp.validation;

import com.ccp.decorators.CcpJsonRepresentation;

public class CcpJsonFieldsRangeObjectNumberValidation {

	public final CcpJsonRepresentation content;
	public final String[] fields;
	

	//json.validate().range().object().asNumber()
	
	public boolean isGreaterThan(double limit) {

		for (String field : fields) {
			Double asDoubleNumber = this.content.getAsDoubleNumber(field);
			if(asDoubleNumber < limit) {
				return false;
			}
		}
		return true;
	}
	protected CcpJsonFieldsRangeObjectNumberValidation(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}
	public boolean isGreaterOrEqualsTo(double limit) {

		for (String field : this.fields) {
			Double asDoubleNumber = this.content.getAsDoubleNumber(field);
			if(asDoubleNumber <= limit) {
				return false;
			}
		}
		return true;
	}
	public boolean isLessThan(double limit) {

		for (String field : this.fields) {
			Double asDoubleNumber = this.content.getAsDoubleNumber(field);
			if(asDoubleNumber > limit) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isLessOrEqualsTo(double limit) {

		for (String field : this.fields) {
			Double asDoubleNumber = this.content.getAsDoubleNumber(field);
			if(asDoubleNumber >= limit) {
				return false;
			}
		}
		return true;
	}

	public boolean isEqualsTo(double limit) {

		for (String field : this.fields) {
			Double asDoubleNumber = this.content.getAsDoubleNumber(field);
			if(asDoubleNumber != limit) {
				return false;
			}
		}
		return true;
	}
}

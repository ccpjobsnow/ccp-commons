package com.ccp.validation;

import com.ccp.decorators.CcpJsonRepresentation;

public class RangeSize {
	public final CcpJsonRepresentation content;
	public final String[] fields;
	
	public RangeSize(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}

	public boolean equalsTo(int i) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean equalsOrGreaterThan(int i) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean equalsOrLessThan(int i) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean greaterThan(int i) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean lessThan(int i) {
		// TODO Auto-generated method stub
		return false;
	}

}

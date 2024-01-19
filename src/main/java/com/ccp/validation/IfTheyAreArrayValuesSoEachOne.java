package com.ccp.validation;

import com.ccp.decorators.CcpJsonRepresentation;

public class IfTheyAreArrayValuesSoEachOne {
	public final CcpJsonRepresentation content;
	public final String[] fields;
	public IfTheyAreArrayValuesSoEachOne(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}
	public boolean hasNonDuplicatedItems() {
		// TODO Auto-generated method stub
		return false;
	}
	public AreOfTheType isOfTheType() {
		return new AreOfTheType(this.content, this.fields);
	}
	public RangeSize hasTheSizeThatIs() {
		return new RangeSize(this.content, this.fields);
	}
	public boolean isTextAndItIsContainedAtTheList(String...args) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean isNumberAndItIsContainedAtTheList(double...args) {
		// TODO Auto-generated method stub
		return false;
	}
	public IfTheyAreArrayValuesSoEachOne isTextAnd() {
		return new IfTheyAreArrayValuesSoEachOne(this.content, this.fields);
	}
	public RangeSize isNumberAndItIs() {
		return new RangeSize(this.content, this.fields);
	}

	
}

package com.ccp.validation;

import java.util.Collection;
import java.util.List;

import com.ccp.decorators.CcpJsonRepresentation;

public class CcpJsonFieldsRestrictedNumbersValidation {

	public final CcpJsonRepresentation content;
	public final String[] fields;

	protected CcpJsonFieldsRestrictedNumbersValidation(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}

	public boolean inArrayFields(Collection<Double> allowedValues) {
		for (String field : this.fields) {
			List<Object> array = this.content.getAsObjectList(field);
			for (Object item : array) {
				Double value = Double.valueOf("" + item);
				boolean thisValueIsNotAllowed = allowedValues.contains(value) == false;
				if(thisValueIsNotAllowed) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean inFields(Collection<Double> allowedValues) {
		for (String field : this.fields) {
			Double value = this.content.getAsDoubleNumber(field);
			boolean thisValueIsNotAllowed = allowedValues.contains(value) == false;
			if(thisValueIsNotAllowed) {
				return false;
			}
		}
		return true;
	}
	
}

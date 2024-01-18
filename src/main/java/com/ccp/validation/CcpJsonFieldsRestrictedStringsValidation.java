package com.ccp.validation;

import java.util.Collection;
import java.util.List;

import com.ccp.decorators.CcpJsonRepresentation;

public class CcpJsonFieldsRestrictedStringsValidation {

	public final CcpJsonRepresentation content;
	public final String[] fields;

	protected CcpJsonFieldsRestrictedStringsValidation(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}
	//TODO REVER NOME
	public boolean inArrayFields(Collection<String> allowedValues) {
		for (String field : this.fields) {
			List<Object> array = this.content.getAsObjectList(field);
			for (Object item : array) {
				String value = "" + item;
				boolean thisValueIsNotAllowed = allowedValues.contains(value) == false;
				if(thisValueIsNotAllowed) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean inFields(Collection<String> allowedValues) {
		for (String field : this.fields) {
			String value = this.content.getAsString(field);
			boolean thisValueIsNotAllowed = allowedValues.contains(value) == false;
			if(thisValueIsNotAllowed) {
				return false;
			}
		}
		return true;
	}

}

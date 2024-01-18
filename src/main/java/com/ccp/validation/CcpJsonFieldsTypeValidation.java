package com.ccp.validation;

import com.ccp.decorators.CcpJsonRepresentation;

public class CcpJsonFieldsTypeValidation {

	public final CcpJsonRepresentation content;
	public final String[] fields;
	
	protected CcpJsonFieldsTypeValidation(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}

	public boolean areLong() {
		for (String field : this.fields) {
			boolean x = this.content.getAsMetadata(field).isLongNumber();
			if(x == false) {
				return false;
			}
			
		}
		return true;

	}
	
	public boolean areDouble() {
		for (String field : this.fields) {
			boolean x = this.content.getAsMetadata(field).isDoubleNumber();
			if(x == false) {
				return false;
			}
			
		}
		return true;
	
	}
	
	public boolean areBoolean() {
		for (String field : this.fields) {
			boolean x = this.content.getAsMetadata(field).isBoolean();
			if(x == false) {
				return false;
			}
			
		}
		return true;
		
	}

	public boolean areList() {
		for (String field : this.fields) {
			boolean x = this.content.getAsMetadata(field).isList();
			if(x == false) {
				return false;
			}
			
		}
		return true;
		
	}
	public boolean areJson() {
		for (String field : this.fields) {
			boolean x = this.content.getAsMetadata(field).isJSon();
			if(x == false) {
				return false;
			}
			
		}
		return true;
	}

}

package com.ccp.especifications.db.utils.decorators;

import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpTimeDecorator;
import com.ccp.especifications.db.utils.CcpEntityField;

public class CcpAddTimeFields implements Function<CcpJsonRepresentation, CcpJsonRepresentation> {

	public static CcpAddTimeFields INSTANCE = new CcpAddTimeFields();
	
	private CcpAddTimeFields() {}
	
	public CcpJsonRepresentation apply(CcpJsonRepresentation json) {
		CcpTimeDecorator ctd = new CcpTimeDecorator();
		String formattedDateTime = ctd.getFormattedDateTime(CcpEntityExpurgableOptions.millisecond.format);
		boolean containsAllFields = json.containsAllFields(CcpEntityField.TIMESTAMP.name());
		
		if(containsAllFields) {
			return json;
		}
		
		CcpJsonRepresentation put = json.put(CcpEntityField.TIMESTAMP.name(), ctd.time).put(CcpEntityField.DATE.name(), formattedDateTime);
		
		return put;
	}

}

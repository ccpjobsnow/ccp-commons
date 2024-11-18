package com.ccp.validation.enums;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpTimeDecorator;

public interface TimeEnlapsedValidations extends BoundValidations {
	default Double getDifference(CcpJsonRepresentation json, String field) {
		Double value = json.getAsDoubleNumber(field);
		CcpTimeDecorator ctd = new CcpTimeDecorator();
		int currentYear = ctd.getYear();
		double diff = currentYear - value;
		return diff;
	}

	
	default boolean isValidJson(CcpJsonRepresentation json, double bound, String... fields) {
		boolean fieldsIsNotPresent = json.containsAllFields(fields) == false;
		if(fieldsIsNotPresent) {
			return true;
		}
		for (String field : fields) {
			Double difference = this.getDifference(json, field);
			boolean true1 = this.isTrue(bound, difference);
			if(true1) {
				continue;
			}
			return false;
		}
		return true;
	}

	boolean isTrue(Double bound, Double difference) ;
}

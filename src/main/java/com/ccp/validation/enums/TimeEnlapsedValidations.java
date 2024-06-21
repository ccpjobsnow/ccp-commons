package com.ccp.validation.enums;

import java.util.function.Function;
import java.util.function.Predicate;

import com.ccp.decorators.CcpJsonRepresentation;

public interface TimeEnlapsedValidations {
	
	default boolean isTrue(CcpJsonRepresentation json, Predicate<Double> predicate,
			Function<Double, Double> function,
			String... fields) {
		
		for (String field : fields) {
			boolean fieldIsNotPresent = json.containsAllFields(field) == false;
			if(fieldIsNotPresent) {
				continue;
			}
			Double value = json.getAsDoubleNumber(field);
			double enlapsed = function.apply(value);
			boolean isTrue = predicate.test(enlapsed);
			
			if(isTrue) {
				return true;
			}
		}
		return false;
	}

}

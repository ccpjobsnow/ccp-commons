package com.ccp.validation.enums;

import java.util.Calendar;
import java.util.function.Predicate;

import com.ccp.decorators.CcpJsonRepresentation;

public enum DayValidations implements BoundValidations{
	equalsTo 
	{
		public boolean isValidJson(CcpJsonRepresentation json, double bound, String... fields) {
			boolean isTrue = super.isTrue(json, value -> Double.valueOf(bound).equals(value), fields);
			
			return isTrue;
		}
	},
	equalsOrGreaterThan
	{
		public boolean isValidJson(CcpJsonRepresentation json, double bound, String... fields) {
			boolean isTrue = super.isTrue(json, value -> bound >= value, fields);
			
			return isTrue;
		}
	},
	equalsOrLessThan
	{
		public boolean isValidJson(CcpJsonRepresentation json, double bound, String... fields) {
			boolean isTrue = super.isTrue(json, value -> bound <= value, fields);
			
			return isTrue;
		}
	},
	greaterThan
	{
		public boolean isValidJson(CcpJsonRepresentation json, double bound, String... fields) {
			boolean isTrue = super.isTrue(json, value -> bound > value, fields);
			
			return isTrue;
		}
	},
	lessThan
	{
		public boolean isValidJson(CcpJsonRepresentation json, double bound, String... fields) {
			boolean isTrue = super.isTrue(json, value -> bound < value, fields);
			
			return isTrue;
		}
	},
	;
	
	protected boolean isTrue(CcpJsonRepresentation json, Predicate<Double> predicate , String... fields) {
		
	
		for (String field : fields) {
			Double value = json.getAsDoubleNumber(field);
			double enlapsed = value - System.currentTimeMillis();
			double days = enlapsed / (24 * 60 * 60 * 1000);
			boolean isTrue = predicate.test(days);
			
			if(isTrue) {
				return true;
			}
		}
		return false;
	}

}

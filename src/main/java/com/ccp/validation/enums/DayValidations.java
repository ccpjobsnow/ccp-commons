package com.ccp.validation.enums;

import java.util.function.Predicate;

import com.ccp.decorators.CcpJsonRepresentation;

public enum DayValidations implements BoundValidations, TimeEnlapsedValidations{
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
	
		boolean true1 = this.isTrue(json, predicate, value -> (value - System.currentTimeMillis()) / 86_400_000, fields);
		
		return true1;
	}

}

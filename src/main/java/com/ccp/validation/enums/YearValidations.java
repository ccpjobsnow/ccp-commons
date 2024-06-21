package com.ccp.validation.enums;

import java.util.Calendar;
import java.util.function.Predicate;

import com.ccp.decorators.CcpJsonRepresentation;

public enum YearValidations implements BoundValidations, TimeEnlapsedValidations{
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
			boolean fieldsIsNotPresent = json.containsAllFields(fields) == false;
			if(fieldsIsNotPresent) {
				return true;
			}
			for (String field : fields) {
				Double asDoubleNumber = json.getAsDoubleNumber(field);
				if(bound <= asDoubleNumber) {
					continue;
				}
				return false;
			}
			return true;
		}
	},
	equalsOrLessThan
	{
		public boolean isValidJson(CcpJsonRepresentation json, double bound, String... fields) {
			boolean fieldsIsNotPresent = json.containsAllFields(fields) == false;
			if(fieldsIsNotPresent) {
				return true;
			}
			for (String field : fields) {
				Double asDoubleNumber = json.getAsDoubleNumber(field);
				if(bound >= asDoubleNumber) {
					continue;
				}
				return false;
			}
			return true;
		}
	},
	greaterThan
	{
		public boolean isValidJson(CcpJsonRepresentation json, double bound, String... fields) {
			boolean fieldsIsNotPresent = json.containsAllFields(fields) == false;
			if(fieldsIsNotPresent) {
				return true;
			}
			for (String field : fields) {
				Double asDoubleNumber = json.getAsDoubleNumber(field);
				if(bound < asDoubleNumber) {
					continue;
				}
				return false;
			}
			return true;
		}
	},
	lessThan
	{
		public boolean isValidJson(CcpJsonRepresentation json, double bound, String... fields) {
			boolean fieldsIsNotPresent = json.containsAllFields(fields) == false;
			if(fieldsIsNotPresent) {
				return true;
			}
			for (String field : fields) {
				Double asDoubleNumber = json.getAsDoubleNumber(field);
				if(bound > asDoubleNumber) {
					continue;
				}
				return false;
			}
			return true;
		}
	},
	;
	
	protected boolean isTrue(CcpJsonRepresentation json, Predicate<Double> predicate , String... fields) {
		
		Calendar instance = Calendar.getInstance();
		int currentYear = instance.get(Calendar.YEAR);

		boolean true1 = this.isTrue(json, predicate, value -> currentYear - value, fields);
		
		return true1;
	}

}

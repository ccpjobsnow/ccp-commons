package com.ccp.validation.enums;

import com.ccp.decorators.CcpJsonRepresentation;

public enum ObjectValidations {
	requiredFields {
		
		public boolean isValidJson(CcpJsonRepresentation json, String... fields) {
			return json.containsAllKeys(fields);
		}
	},
	requiredAtLeastOne {
		
		public boolean isValidJson(CcpJsonRepresentation json, String... fields) {
			return json.containsAnyKeys(fields);
		}
	},
	booleanFields {
		
		public boolean isValidJson(CcpJsonRepresentation json, String... fields) {
			return json.itIsTrueThatTheFollowingFields(fields).areAllOfTheType().bool();
		}
	},
	doubleFields {
		
		public boolean isValidJson(CcpJsonRepresentation json, String... fields) {
			return json.itIsTrueThatTheFollowingFields(fields).areAllOfTheType().doubleNumber();
		}
	},
	jsonFields {
		
		public boolean isValidJson(CcpJsonRepresentation json, String... fields) {
			return json.itIsTrueThatTheFollowingFields(fields).areAllOfTheType().json();
		}
	},
	listFields {
		
		public boolean isValidJson(CcpJsonRepresentation json, String... fields) {
			return json.itIsTrueThatTheFollowingFields(fields).areAllOfTheType().list();
		}
	},
	integerFields {
		
		public boolean isValidJson(CcpJsonRepresentation json, String... fields) {
			return json.itIsTrueThatTheFollowingFields(fields).areAllOfTheType().longNumber();
		}
	},
	nonRepeatedLists {
		
		public boolean isValidJson(CcpJsonRepresentation json, String... fields) {
			return json.itIsTrueThatTheFollowingFields(fields).ifTheyAreAllArrayValuesThenEachOne().hasNonDuplicatedItems();
		}
	},
	;
	public abstract boolean isValidJson(CcpJsonRepresentation json, String... fields);
}

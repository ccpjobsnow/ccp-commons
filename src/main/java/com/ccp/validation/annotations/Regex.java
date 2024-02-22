package com.ccp.validation.annotations;

public @interface Regex {
	String[] allowedValues();
	String[] fields();
	String value();
}

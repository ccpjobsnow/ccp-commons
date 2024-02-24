package com.ccp.validation.annotations;

public @interface Regex {
	String[] fields();
	String value();
}

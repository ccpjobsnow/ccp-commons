package com.ccp.validation.annotations;

import com.ccp.validation.enums.ArrayValidations;

public @interface ArrayRules {
	ArrayValidations rule ();
	String[] fields();
}

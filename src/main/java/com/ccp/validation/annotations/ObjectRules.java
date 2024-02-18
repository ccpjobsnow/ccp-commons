package com.ccp.validation.annotations;

import com.ccp.validation.enums.ObjectValidations;

public @interface ObjectRules {
	ObjectValidations rule ();
	String[] fields();
}

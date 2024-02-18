package com.ccp.validation.annotations;

import com.ccp.validation.enums.ArrayTextsSizeValidations;

public @interface ArrayTexts {
	ArrayTextsSizeValidations rule();
	String[] fields();
	double bound();

}

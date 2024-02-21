package com.ccp.validation.annotations;

import com.ccp.validation.enums.CurrentYearValidations;

public @interface CurrentYear {
	CurrentYearValidations rule();
	String[] fields();
	int bound();

}

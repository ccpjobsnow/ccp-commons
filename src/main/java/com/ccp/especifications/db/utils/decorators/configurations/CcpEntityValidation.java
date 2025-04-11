package com.ccp.especifications.db.utils.decorators.configurations;

public @interface CcpEntityValidation {
	Class<?> jsonValidationClass();
	Class<?>[] beforeOperation();
	Class<?>[] afterOperation();
}

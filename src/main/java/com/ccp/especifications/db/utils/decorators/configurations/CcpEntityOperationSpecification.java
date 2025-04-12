package com.ccp.especifications.db.utils.decorators.configurations;

public @interface CcpEntityOperationSpecification {
	Class<?> classWithFieldsValidationsRules();
	Class<?>[] beforeOperation();
	Class<?>[] afterOperation();
}

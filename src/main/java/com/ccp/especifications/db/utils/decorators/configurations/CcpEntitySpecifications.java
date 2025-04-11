package com.ccp.especifications.db.utils.decorators.configurations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface CcpEntitySpecifications {

	CcpEntityValidation changeStatus();
	CcpEntityValidation delete();
	CcpEntityValidation save();

	boolean cacheableEntity();
	
}

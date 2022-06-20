package com.ccp.dependency.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CcpEspecification {
	
	Class<? extends CcpImplementationProvider> value() default DefaultImplementationProvider.class;
	
	public static abstract class DefaultImplementationProvider implements CcpImplementationProvider {}
}

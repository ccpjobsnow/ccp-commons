package com.ccp.validation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface ValidationRules {

	ObjectRules[] simpleObjectRules() default {};

	ArrayRules[] simpleArrayRules() default {};

	AllowedValues[] allowedValues() default {};

	ObjectNumbers[] objectNumbersValidations() default {};

	ArrayNumbers[] arrayNumbersValidations() default {};

	ObjectText[] objectTextsValidations() default {};

	ArrayTexts[] arrayTextsValidations() default {};

	ArraySize[] arraySizeValidations() default {};
	
	CurrentYear[] currentYearValidations() default {};

	Day[] dayValidations() default {};
	
	Class<?> rulesClass() default Dumb.class;
	
	class Dumb {

	}

}

package com.ccp.exceptions.json;

@SuppressWarnings("serial")
public class JsonFieldIncorrectType extends RuntimeException{
	public JsonFieldIncorrectType(String field, Class<?> expectedType, Class<?> actuallyType) {
		super(String.format("The field '%s' must be of type '%s', but is of the type '%s'", field, expectedType.getName(), actuallyType.getName()));
	}
}

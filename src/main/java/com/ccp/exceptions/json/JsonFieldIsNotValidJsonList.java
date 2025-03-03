package com.ccp.exceptions.json;

import java.util.Arrays;

import com.ccp.decorators.CcpJsonRepresentation;

@SuppressWarnings("serial")
public class JsonFieldIsNotValidJsonList extends RuntimeException {
	public JsonFieldIsNotValidJsonList(CcpJsonRepresentation json, Class<?> clazz, String... path) {
		super("The field path '" + Arrays.asList(path) + "' in the json does not represent a json type, instead, it represents a '" + clazz.getName() + "' in the json " + json);
	}
}

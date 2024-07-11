package com.ccp.json.transformers;

import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;

public class CcpJsonTransformerGenerateFieldHash implements Function<CcpJsonRepresentation, CcpJsonRepresentation>{

	private final String oldField;

	private final String newField;
	
	
	public CcpJsonTransformerGenerateFieldHash(String oldField, String newField) {
		this.oldField = oldField;
		this.newField = newField;
	}

	public CcpJsonRepresentation apply(CcpJsonRepresentation json) {
		String email = json.getAsString(this.oldField);
		String hash = new CcpStringDecorator(email).hash().asString("SHA1");
		CcpJsonRepresentation put = json.put(this.oldField, hash).put(this.newField, email);
		return put;
	}
}

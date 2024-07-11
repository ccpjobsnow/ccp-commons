package com.ccp.json.transformers;

import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;

public class CcpJsonTransformerGenerateRandomToken implements Function<CcpJsonRepresentation, CcpJsonRepresentation>{

	private final int size;
	
	private final String fieldName;



	public CcpJsonTransformerGenerateRandomToken(int size, String fieldName) {
		this.fieldName = fieldName;
		this.size = size;
	}



	public CcpJsonRepresentation apply(CcpJsonRepresentation json) {

		final String CHARACTERS_TO_GENERATE_TOKEN = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		
		String token = new CcpStringDecorator(CHARACTERS_TO_GENERATE_TOKEN).text().generateToken(this.size).content;
		
		CcpJsonRepresentation put = json.put(this.fieldName, token);
		
		return put;
	}
}

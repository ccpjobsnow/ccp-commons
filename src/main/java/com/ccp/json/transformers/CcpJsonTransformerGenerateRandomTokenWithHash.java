package com.ccp.json.transformers;

import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.password.CcpPasswordHandler;

public class CcpJsonTransformerGenerateRandomTokenWithHash implements Function<CcpJsonRepresentation, CcpJsonRepresentation>{

	private final int size;
	
	private final String fieldName;

	private final String fieldHashName;
	
	public CcpJsonTransformerGenerateRandomTokenWithHash(int size, String fieldName, String fieldHashName) {
		this.fieldHashName = fieldHashName;
		this.fieldName = fieldName;
		this.size = size;
	}

	public CcpJsonRepresentation apply(CcpJsonRepresentation json) {

		CcpJsonTransformerGenerateRandomToken transformer = new CcpJsonTransformerGenerateRandomToken(this.size, this.fieldName);
	
		CcpJsonRepresentation transformed = json.getTransformed(transformer);
		
		String token = transformed.getAsString(this.fieldName);
		
		CcpPasswordHandler dependency = CcpDependencyInjection.getDependency(CcpPasswordHandler.class);
		
		String tokenHash = dependency.getHash(token);
		
		CcpJsonRepresentation put = transformed.put(this.fieldHashName, tokenHash);
		
		return put;
	}
}

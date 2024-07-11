package com.ccp.json.transformers;

import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.password.CcpPasswordHandler;

public class CcpJsonTransformerPutPasswordField implements Function<CcpJsonRepresentation, CcpJsonRepresentation> {

	private final String passwordField;
	
	public CcpJsonTransformerPutPasswordField(String passwordField) {
		this.passwordField = passwordField;
	}



	public CcpJsonRepresentation apply(CcpJsonRepresentation json) {
		
		String passwordValue = json.getAsString(this.passwordField);
		
		CcpPasswordHandler dependency = CcpDependencyInjection.getDependency(CcpPasswordHandler.class);
		
		String passwordHash = dependency.getHash(passwordValue);
		
		CcpJsonRepresentation jsonPassword = json.put(this.passwordField, passwordHash);
		
		return jsonPassword;
	}

}

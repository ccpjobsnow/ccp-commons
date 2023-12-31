package com.ccp.exceptions.process;

import com.ccp.decorators.CcpJsonRepresentation;

public class CcpThrowException implements  java.util.function.Function<CcpJsonRepresentation, CcpJsonRepresentation>{

	private final RuntimeException exception;
	
	public CcpThrowException(RuntimeException exception) {
		this.exception = exception;
	}

	public CcpJsonRepresentation apply(CcpJsonRepresentation values) {
		throw this.exception;
	}

}

package com.ccp.exceptions.process;

import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;

public class CcpThrowException implements  Function<CcpJsonRepresentation, CcpJsonRepresentation>{

	private final RuntimeException exception;
	
	public CcpThrowException(RuntimeException exception) {
		this.exception = exception;
	}

	public CcpJsonRepresentation apply(CcpJsonRepresentation values) {
		throw this.exception;
	}

}

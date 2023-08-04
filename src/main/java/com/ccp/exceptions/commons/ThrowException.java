package com.ccp.exceptions.commons;

import com.ccp.decorators.CcpMapDecorator;

public class ThrowException implements  java.util.function.Function<CcpMapDecorator, CcpMapDecorator>{

	private final RuntimeException exception;
	
	public ThrowException(RuntimeException exception) {
		this.exception = exception;
	}

	public CcpMapDecorator apply(CcpMapDecorator values) {
		throw this.exception;
	}

}

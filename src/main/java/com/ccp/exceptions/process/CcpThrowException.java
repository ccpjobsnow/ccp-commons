package com.ccp.exceptions.process;

import com.ccp.decorators.CcpMapDecorator;

public class CcpThrowException implements  java.util.function.Function<CcpMapDecorator, CcpMapDecorator>{

	private final RuntimeException exception;
	
	public CcpThrowException(RuntimeException exception) {
		this.exception = exception;
	}

	public CcpMapDecorator apply(CcpMapDecorator values) {
		throw this.exception;
	}

}

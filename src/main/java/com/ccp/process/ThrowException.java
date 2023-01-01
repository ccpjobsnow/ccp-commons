package com.ccp.process;

import com.ccp.decorators.CcpMapDecorator;

public class ThrowException implements CcpProcess{

	private final RuntimeException exception;
	
	public ThrowException(RuntimeException exception) {
		this.exception = exception;
	}

	public CcpMapDecorator execute(CcpMapDecorator values) {
		throw this.exception;
	}

}

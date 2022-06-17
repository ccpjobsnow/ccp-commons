package com.ccp.process;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.exceptions.process.CcpStepResult;

@SuppressWarnings("serial")
public class CcpFlow extends RuntimeException {

	public final CcpMapDecorator data;
	
	public final int status;
	
	public CcpFlow(CcpStepResult stepResult) {
		this.data = stepResult.getData();
		this.status = stepResult.status;
	}
	
}

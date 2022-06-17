package com.ccp.exceptions.process;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpStepResult;

@SuppressWarnings("serial")
public class CcpFlow extends RuntimeException {

	public final CcpMapDecorator data;
	
	public final int status;
	
	public CcpFlow(CcpStepResult stepResult) {
		this.data = stepResult.getData();
		this.status = stepResult.status;
	}
	
}

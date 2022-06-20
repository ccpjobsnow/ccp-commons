package com.ccp.exceptions.process;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpStepResult;

@SuppressWarnings("serial")
public class CcpFlowDeviation extends RuntimeException {

	public final CcpMapDecorator data;
	
	public final int status;
	
	public CcpFlowDeviation(CcpStepResult stepResult) {
		this.data = stepResult.getData();
		this.status = stepResult.status;
	}
	
}

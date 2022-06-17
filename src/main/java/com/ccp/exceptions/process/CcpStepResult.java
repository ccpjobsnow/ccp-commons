package com.ccp.exceptions.process;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpNextStepFactory;

public class CcpStepResult{

	public final CcpMapDecorator data;
	
	public final String businessName;

	public final String flowName;

	public final int status;

	public CcpStepResult(CcpMapDecorator data, int status, CcpNextStepFactory flow) {
		CcpMapDecorator pastSteps = data.getInternalMap("pastSteps");
		String flowName = flow.getClass().getName();
		
		if(pastSteps.containsAllKeys(flowName)) {
			throw new RuntimeException("It is not allowed repeated steps: " + flowName);
		}
		pastSteps = pastSteps.put(flowName, data.removeKey("pastSteps"));
		this.data = data.put("pastSteps", pastSteps);
		this.businessName = flow.businessName;
		this.flowName = flowName;
		this.status = status;
	}

	public CcpMapDecorator getData() {
		return this.data.put("businessName", this.businessName).put("flowName", this.flowName);
	}
	
	
}

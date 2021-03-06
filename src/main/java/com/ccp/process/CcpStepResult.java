package com.ccp.process;

import com.ccp.decorators.CcpMapDecorator;

public class CcpStepResult{

	public final CcpMapDecorator data;
	
	public final String business;

	public final String step;

	public final int status;

	public CcpStepResult(CcpMapDecorator data, int status, CcpNextStep currentStep) {

		CcpMapDecorator pastSteps = data.getInternalMap("pastSteps");
		
		String step = currentStep.getClass().getName();
		
		pastSteps = pastSteps.put(step, data.removeKey("pastSteps"));
		
		this.data = data.put("pastSteps", pastSteps);
		this.business = currentStep.businessName;
		this.status = status;
		this.step = step;
	}

	public CcpMapDecorator getData() {
		return this.data.put("business", this.business).put("step", this.step);
	}
	
	
}

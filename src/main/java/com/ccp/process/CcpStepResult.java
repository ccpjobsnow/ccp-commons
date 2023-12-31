package com.ccp.process;

import com.ccp.decorators.CcpJsonRepresentation;

public class CcpStepResult{

	public final CcpJsonRepresentation values;
	
	public final String business;

	public final String step;

	public final int status;

	public CcpStepResult(CcpJsonRepresentation data, int status, CcpNextStep currentStep) {

		CcpJsonRepresentation pastSteps = data.getInnerJson("pastSteps");
		
		String step = currentStep.getClass().getName();
		
		pastSteps = pastSteps.put(step, data.removeKey("pastSteps"));
		
		this.values = data.put("pastSteps", pastSteps);
		this.business = currentStep.businessName;
		this.status = status;
		this.step = step;
	}

	public CcpJsonRepresentation getData() {
		return this.values.put("business", this.business).put("step", this.step);
	}
	
	
}

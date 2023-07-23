package com.ccp.process;

import java.util.HashMap;
import java.util.Map;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.exceptions.commons.CcpFlow;

public abstract class CcpNextStep {
	
	public final String businessName;
	
	private final Map<Integer, CcpNextStep> decisionTree;

	public CcpNextStep(String businessName) {
		this.businessName = businessName;
		this.decisionTree = new HashMap<>();
	}
	
	public CcpNextStep() {
		this.businessName = this.getClass().getSimpleName();
		this.decisionTree = new HashMap<>();
		
	}
	
	public CcpNextStep addStep(int status, CcpNextStep nextProcess) {
		this.decisionTree.put(status, nextProcess);
		return this;
	}
	
	public final CcpStepResult goToTheNextStep(CcpMapDecorator currentValues) {
		
		CcpStepResult stepResult = this.executeThisStep(currentValues);
		
		boolean thisProcessHasFinishedWithSuccess = this.decisionTree.isEmpty();
		
		if(thisProcessHasFinishedWithSuccess) {
			return new CcpStepResult(stepResult.values, stepResult.status, this);
		}

		CcpNextStep nextStep = this.decisionTree.get(stepResult.status);
		
		boolean thisProcessHasFinishedWithError = nextStep == null;

		if(thisProcessHasFinishedWithError) {
			throw new CcpFlow(stepResult);
		}
		
		CcpStepResult execute = nextStep.goToTheNextStep(stepResult.values);
		return execute;
	}

	public abstract CcpStepResult executeThisStep(CcpMapDecorator values);

	
	
	
}

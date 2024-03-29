package com.ccp.process;

import java.util.HashMap;
import java.util.Map;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.exceptions.process.CcpFlow;

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
	
	public CcpNextStep addEmptyStep() {
		return this.addAlternativeStep(new CcpSuccessStatus(), null);
	}
	public CcpNextStep addMostExpectedStep(CcpNextStep nextProcess) {
		return this.addAlternativeStep(new CcpSuccessStatus(), nextProcess);
	}
	public CcpNextStep addAlternativeStep(CcpProcessStatus status, CcpNextStep nextProcess) {
		this.decisionTree.put(status.status(), nextProcess);
		return this;
	}
	
	public final CcpStepResult goToTheNextStep(CcpJsonRepresentation currentValues) {
		
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

	public abstract CcpStepResult executeThisStep(CcpJsonRepresentation values);

	
	public String toString() {
		return this.businessName;
	}
	
}

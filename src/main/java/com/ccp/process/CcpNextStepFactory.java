package com.ccp.process;

import java.util.HashMap;
import java.util.Map;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.exceptions.process.CcpFlow;

public abstract class CcpNextStepFactory {
	
	public final String businessName;
	
	private final Map<Integer, CcpNextStepFactory> decisionTree;

	public CcpNextStepFactory(String businessName) {
		this.businessName = businessName;
		this.decisionTree = new HashMap<>();
	}
	
	public CcpNextStepFactory addStep(int status, CcpNextStepFactory nextProcess) {
		this.decisionTree.put(status, nextProcess);
		return this;
	}
	
	public CcpStepResult executeAllSteps(CcpMapDecorator values) {
		CcpStepResult executeDecisionTree = this.executeTheNextStep(values);
		return executeDecisionTree;
	}

	private CcpStepResult executeTheNextStep(CcpMapDecorator currentValues) {
		
		CcpStepResult stepResult = this.executeDecisionTree(currentValues);
		
		boolean thereAreNotNextSteps = this.decisionTree.isEmpty();
		
		if(thereAreNotNextSteps) {
			return new CcpStepResult(stepResult.data, stepResult.status, this);
		}

		CcpNextStepFactory nextProcess = this.decisionTree.get(stepResult.status);
		boolean unexpectedStatus = nextProcess == null;

		if(unexpectedStatus) {
			throw new CcpFlow(stepResult);
		}
		
		CcpStepResult execute = nextProcess.executeTheNextStep(stepResult.data);
		return execute;
	}

	public abstract CcpStepResult executeDecisionTree(CcpMapDecorator values);

	
	
	
}

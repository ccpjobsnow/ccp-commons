package com.ccp.flow;

import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.exceptions.process.CcpFlow;
import com.ccp.process.CcpProcessStatus;

public final class AndIfThisExecutionReturns {
	protected final Function<CcpJsonRepresentation, CcpJsonRepresentation> givenFinalTargetProcess;
	protected final CcpJsonRepresentation givenJson;
	protected final CcpJsonRepresentation flow;

	protected AndIfThisExecutionReturns(Function<CcpJsonRepresentation, CcpJsonRepresentation> givenFinalTargetProcess,
			CcpJsonRepresentation givenJson, CcpJsonRepresentation flow) {

		this.givenFinalTargetProcess = givenFinalTargetProcess;
		this.givenJson = givenJson;
		this.flow = flow;
	}
	
	
	public IfThisExecutionReturns ifThisExecutionReturns(CcpProcessStatus processStatus) {
		return new IfThisExecutionReturns(this.givenFinalTargetProcess, this.givenJson, processStatus, this.flow);
	}
	
	public CcpJsonRepresentation endThisStatement() {
		try {
			CcpJsonRepresentation responseWhenTheFlowPerformsNormally = this.givenFinalTargetProcess.apply(this.givenJson);
			return responseWhenTheFlowPerformsNormally;
		} catch (CcpFlow e) {
			this.tryToFixTheFlow(e);
			CcpJsonRepresentation responseWhenTheFlowWasFixed = this.givenFinalTargetProcess.apply(this.givenJson);
			return responseWhenTheFlowWasFixed;
		}
	}

	private void tryToFixTheFlow(CcpFlow e) {
		
		Function<CcpJsonRepresentation, CcpJsonRepresentation>[] nextFlows = this.flow.getAsObject(e.status.name());
		CcpJsonRepresentation json = this.givenJson;
		
		CcpJsonRepresentation flowWithoutCurrentProcessStatus = this.flow.removeField(e.status.name());

		for (Function<CcpJsonRepresentation, CcpJsonRepresentation> nextFlow : nextFlows) {
			AndIfThisExecutionReturns andIfThisExecutionReturns = new AndIfThisExecutionReturns(nextFlow, json, flowWithoutCurrentProcessStatus);
			json = andIfThisExecutionReturns.endThisStatement();
		}
	}

}

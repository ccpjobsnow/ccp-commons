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
			CcpJsonRepresentation responseWhenTheFlowWasFixed = this.givenFinalTargetProcess.apply(this.givenJson);
			return responseWhenTheFlowWasFixed;
		} catch (CcpFlow e) {
			this.tryToFixTheFlow(e);
			CcpJsonRepresentation endThisStatement = this.endThisStatement();
			return endThisStatement;
		}
	}

	private void tryToFixTheFlow(CcpFlow e) {
		Function<CcpJsonRepresentation, CcpJsonRepresentation> nextFlow = this.flow.getAsObject(e.status.name());
		nextFlow.apply(this.givenJson);
	}

}

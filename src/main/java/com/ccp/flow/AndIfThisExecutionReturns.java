package com.ccp.flow;

import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.exceptions.process.CcpFlowDiversion;
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
	
	@SuppressWarnings("unchecked")
	public CcpJsonRepresentation endThisStatement(Function<CcpJsonRepresentation, CcpJsonRepresentation>... whatToNext) {
		try {
			CcpJsonRepresentation responseWhenTheFlowPerformsNormally = this.tryToPerformNormally(whatToNext);
			return responseWhenTheFlowPerformsNormally;
		} catch (CcpFlowDiversion e) {
			CcpJsonRepresentation responseWhenTheFlowWasFixed = this.tryToFixTheFlow(e);
			return responseWhenTheFlowWasFixed;
		}
	}


	@SuppressWarnings("unchecked")
	private CcpJsonRepresentation tryToPerformNormally(
			Function<CcpJsonRepresentation, CcpJsonRepresentation>... whatToNext) {
		CcpJsonRepresentation responseWhenTheFlowPerformsNormally = this.givenFinalTargetProcess.apply(this.givenJson);
		for (Function<CcpJsonRepresentation, CcpJsonRepresentation> function : whatToNext) {
			function.apply(this.givenJson);
		}
		return responseWhenTheFlowPerformsNormally;
	}

	@SuppressWarnings("unchecked")
	private CcpJsonRepresentation tryToFixTheFlow(CcpFlowDiversion e) {
		
		Function<CcpJsonRepresentation, CcpJsonRepresentation>[] nextFlows = this.flow.getAsObject(e.status.name());
		CcpJsonRepresentation json = this.givenJson;
		
		CcpJsonRepresentation flowWithoutCurrentProcessStatus = this.flow.removeField(e.status.name());

		for (Function<CcpJsonRepresentation, CcpJsonRepresentation> nextFlow : nextFlows) {
			AndIfThisExecutionReturns andIfThisExecutionReturns = new AndIfThisExecutionReturns(nextFlow, json, flowWithoutCurrentProcessStatus);
			json = andIfThisExecutionReturns.endThisStatement();
		}
		
		CcpJsonRepresentation responseWhenTheFlowWasFixed = this.givenFinalTargetProcess.apply(this.givenJson);
		return responseWhenTheFlowWasFixed;
	}

}

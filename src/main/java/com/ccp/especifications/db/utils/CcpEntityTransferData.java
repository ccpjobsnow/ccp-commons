package com.ccp.especifications.db.utils;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.process.CcpNextStep;
import com.ccp.process.CcpStepResult;

public class CcpEntityTransferData extends CcpNextStep {

	private final CcpEntity origin;
	private final CcpEntity target;
	
	public CcpEntityTransferData(CcpEntity origin, CcpEntity target) {
		this.origin = origin;
		this.target = target;
	}

	
	public CcpStepResult executeThisStep(CcpJsonRepresentation values) {
		
		CcpJsonRepresentation entities = values.getInnerJson("_entities");

		boolean doNothing = entities.getInnerJson(this.origin.name()).isEmpty() ;
		
		if(doNothing) {
			return new CcpStepResult(values, 200, this);
		}
		
		boolean theOriginDataIsMissing = values.isEmpty();
		
		if(theOriginDataIsMissing) {
			return new CcpStepResult(values, 200, this);
		}
		
		this.origin.transferData(values, this.target);
		CcpJsonRepresentation renameKey = entities.renameKey(this.origin.name(), this.target.name());
		CcpJsonRepresentation put = values.put("_entities", renameKey);
		return new CcpStepResult(put, 200, this);
	}

}

package com.ccp.especifications.db.utils;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpNextStep;
import com.ccp.process.CcpStepResult;

public class TransferDataBetweenEntities extends CcpNextStep {

	private final CcpEntity origin;
	private final CcpEntity target;
	
	public TransferDataBetweenEntities(CcpEntity origin, CcpEntity target) {
		this.origin = origin;
		this.target = target;
	}

	@Override
	public CcpStepResult executeThisStep(CcpMapDecorator values) {
		
		CcpMapDecorator entities = values.getInternalMap("_entities");

		boolean doNothing = entities.getInternalMap(this.origin.name()).isEmpty() ;
		
		if(doNothing) {
			return new CcpStepResult(values, 200, this);
		}
		
		boolean theOriginDataIsMissing = values.isEmpty();
		
		if(theOriginDataIsMissing) {
			return new CcpStepResult(values, 200, this);
		}
		
		this.origin.delete(values);
		this.target.createOrUpdate(values);
		CcpMapDecorator renameKey = entities.renameKey(this.origin.name(), this.target.name());
		CcpMapDecorator put = values.put("_entities", renameKey);
		return new CcpStepResult(put, 200, this);
	}

}

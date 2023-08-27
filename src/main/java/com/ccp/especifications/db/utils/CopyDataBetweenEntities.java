package com.ccp.especifications.db.utils;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpNextStep;
import com.ccp.process.CcpStepResult;

public class CopyDataBetweenEntities extends CcpNextStep {

	private final CcpEntity origin;
	private final CcpEntity target;
	
	public CopyDataBetweenEntities(CcpEntity origin, CcpEntity target) {
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
		
		CcpMapDecorator data = this.origin.getOneById(values);

		boolean theOriginDataIsMissing = data.isEmpty();
		
		if(theOriginDataIsMissing) {
			return new CcpStepResult(values, 200, this);
		}
		
		this.target.createOrUpdate(data);
		CcpMapDecorator renameKey = entities.renameKey(this.origin.name(), this.target.name());
		CcpMapDecorator put = values.put("_entities", renameKey);
		return new CcpStepResult(put, 200, this);
	}

}

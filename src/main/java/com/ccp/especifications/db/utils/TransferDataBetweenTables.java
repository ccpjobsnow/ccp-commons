package com.ccp.especifications.db.utils;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpNextStep;
import com.ccp.process.CcpStepResult;

public class TransferDataBetweenTables extends CcpNextStep {

	private final CcpDbTable origin;
	private final CcpDbTable target;
	
	public TransferDataBetweenTables(CcpDbTable origin, CcpDbTable target) {
		this.origin = origin;
		this.target = target;
	}

	@Override
	public CcpStepResult executeThisStep(CcpMapDecorator values) {
		
		CcpMapDecorator tables = values.getInternalMap("_tables");

		boolean doNothing = tables.getInternalMap(this.origin.name()).isEmpty() ;
		
		if(doNothing) {
			return new CcpStepResult(values, 200, this);
		}
		
		CcpMapDecorator remove = this.origin.remove(values);

		boolean theOriginDataIsMissing = remove.isEmpty();
		
		if(theOriginDataIsMissing) {
			return new CcpStepResult(values, 200, this);
		}
		
		this.target.save(remove);
		CcpMapDecorator renameKey = tables.renameKey(this.origin.name(), this.target.name());
		CcpMapDecorator put = values.put("_tables", renameKey);
		return new CcpStepResult(put, 200, this);
	}

}

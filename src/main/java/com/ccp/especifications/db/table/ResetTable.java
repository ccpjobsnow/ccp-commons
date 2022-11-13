package com.ccp.especifications.db.table;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpNextStep;
import com.ccp.process.CcpStepResult;

public class ResetTable extends CcpNextStep{
	
	private final CcpDbTable entity;
	
	public ResetTable(CcpDbTable entity) {
		this.entity = entity;
	}

	@Override
	public CcpStepResult executeThisStep(CcpMapDecorator values) {
		CcpMapDecorator tables = values.getInternalMap("_tables");

		boolean doNothing = tables.getInternalMap(this.entity.name()).isEmpty();
		
		if(doNothing) {
			return new CcpStepResult(values, 200, this);
		}
		
		this.entity.remove(values);

		CcpMapDecorator removeKey = tables.removeKey(this.entity.name());
		CcpMapDecorator put = values.put("_tables", removeKey);
		return new CcpStepResult(put, 200, this);
	}

}

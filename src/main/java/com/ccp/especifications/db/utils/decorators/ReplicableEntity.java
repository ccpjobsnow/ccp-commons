package com.ccp.especifications.db.utils.decorators;

import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpEntityField;

class ReplicableEntity extends CcpEntityDelegator{
	
	private final String twinEntityName;

	public ReplicableEntity(String twinEntityName, CcpEntity entity) {
		super(entity);
		this.twinEntityName = twinEntityName;
	}

	public final CcpEntity getTwinEntity() {
		CopyableEntity original = new CopyableEntity(this);
		CcpEntityField[] fields = this.getFields();
		BaseEntity copy = new BaseEntity(this.twinEntityName, fields);
		CcpEntity twin = new CopyableEntity(copy);
		TwinEntity twinEntity = new TwinEntity(this.twinEntityName, original, twin);
		return twinEntity;
	}

}

package com.ccp.especifications.db.utils.decorators;

import com.ccp.especifications.db.utils.CcpEntity;

class ReplicableEntity extends CcpDelegatorEntity{
	private final String twinEntityName;

	public ReplicableEntity(String twinEntityName, CcpEntity entity) {
		super(entity);
		this.twinEntityName = twinEntityName;
	}

	public final CcpEntity getTwinEntity() {
		CopyableEntity parent = new CopyableEntity(this);
		TwinEntity twinEntity = new TwinEntity(this.twinEntityName, parent, this.entity);
		return twinEntity;
	}
	
}

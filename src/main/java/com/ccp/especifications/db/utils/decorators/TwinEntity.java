package com.ccp.especifications.db.utils.decorators;

import com.ccp.especifications.db.utils.CcpEntity;

class TwinEntity extends CcpEntityDelegator {

	private final String twinEntityName;
	private final CcpEntity parent;

	public TwinEntity(String twinEntityName, CcpEntity parent, CcpEntity entity) {
		super(entity);
		this.twinEntityName = twinEntityName;
		this.parent = parent;
	}

	public String getEntityName() {
		return this.twinEntityName;
	}

	public CcpEntity getTwinEntity() {
		return this.parent;
	}
}

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
	
	public String[] getEntitiesToSelect() {
		CcpEntity twinEntity = this.getTwinEntity();
		String twinName = twinEntity.getEntityName();
		String entityName = this.getEntityName();
		String[] resourcesNames = new String[]{entityName, twinName};
		return resourcesNames;
	}
	
	public CcpEntity[] getThisEntityAndHisTwinEntity() {
		CcpEntity twinEntity = this.getTwinEntity();
		CcpEntity[] ccpEntities = new CcpEntity[] {this, twinEntity};
		return ccpEntities;
	}

}

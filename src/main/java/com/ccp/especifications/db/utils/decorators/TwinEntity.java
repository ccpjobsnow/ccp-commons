package com.ccp.especifications.db.utils.decorators;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.db.crud.CcpCrud;
import com.ccp.especifications.db.crud.CcpSelectUnionAll;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.exceptions.process.CcpFlow;
import com.ccp.process.CcpProcessStatus;

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
	public CcpJsonRepresentation getInnerJsonFromMainAndTwinEntities(CcpJsonRepresentation json) {
		String entityName = this.getEntityName();
		CcpEntity twinEntity = this.getTwinEntity();
		String twinEntityName = twinEntity.getEntityName();
		CcpJsonRepresentation j1 = json.getInnerJsonFromPath("_entities", entityName);
		CcpJsonRepresentation j2 = json.getInnerJsonFromPath("_entities", twinEntityName);
		CcpJsonRepresentation putAll = j1.putAll(j2);
		return putAll;
	}
	
	public CcpJsonRepresentation getData(CcpJsonRepresentation json) {
		
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		
		CcpSelectUnionAll searchResults = crud.unionBetweenMainAndTwinEntities(json, this);
		
		CcpEntity twinEntity = this.getTwinEntity();

		boolean inactive = twinEntity.isPresentInThisUnionAll(searchResults, json);
		
		if(inactive) {
			CcpJsonRepresentation requiredEntityRow = twinEntity.getRequiredEntityRow(searchResults, json);
			throw new CcpFlow(requiredEntityRow, CcpProcessStatus.INACTIVE_RECORD);
		}
		
		CcpJsonRepresentation requiredEntityRow = this.getRequiredEntityRow(searchResults, json);

		return requiredEntityRow;
	}

}

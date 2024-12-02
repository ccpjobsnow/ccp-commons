package com.ccp.especifications.db.utils.decorators;

import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.crud.CcpSelectUnionAll;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.exceptions.process.CcpFlow;
import com.ccp.process.CcpProcessStatus;

class CopyableEntity  extends CcpDelegatorEntity{
	
	public CopyableEntity(CcpEntity entity) {
		super(entity);
	}

	private void validateTwinEntity(CcpJsonRepresentation json) {
		CcpEntity twinEntity = this.getTwinEntity();
		boolean doesNotExist = twinEntity.exists(json) == false;
		
		if(doesNotExist) {
			return;
		}
		String id = twinEntity.calculateId(json);
		String errorMessage = String.format("The id '%s' has been moved from '%s' to '%s' ", id, this, twinEntity);
		throw new CcpFlow(json, CcpProcessStatus.REDIRECT, errorMessage, new String[0]);
	}
	
	public final CcpJsonRepresentation getOneById(CcpJsonRepresentation json) {
		this.validateTwinEntity(json);
		CcpJsonRepresentation oneById = this.entity.getOneById(json);
		return oneById;
	}
	
	public final CcpJsonRepresentation getOneById(CcpJsonRepresentation json, Function<CcpJsonRepresentation, CcpJsonRepresentation> ifNotFound) {
		this.validateTwinEntity(json);
		CcpJsonRepresentation oneById = this.entity.getOneById(json, ifNotFound);
		return oneById;
	}
	
	public boolean delete(CcpJsonRepresentation json) {
		
		boolean delete = this.entity.delete(json);
		CcpEntity twinEntity = this.getTwinEntity();
		twinEntity.create(json);
		return delete;
	}

	public boolean delete(String id) {
		CcpJsonRepresentation oneById = this.entity.getOneById(id);
		boolean delete = this.entity.delete(id);
		CcpEntity twinEntity = this.getTwinEntity();
		twinEntity.create(oneById);
		return delete;
	}
	
	public final boolean hasTwinEntity() {
		return true;
	}
	
	public CcpJsonRepresentation getRequiredEntityRow(CcpSelectUnionAll unionAll, CcpJsonRepresentation json) {
		CcpEntity twinEntity = this.getTwinEntity();
		
		boolean itIsInTwinEntity = twinEntity.isPresentInThisUnionAll(unionAll, json);
		if(itIsInTwinEntity) {
			CcpJsonRepresentation requiredEntityRow = twinEntity.getRequiredEntityRow(unionAll, json);
			return requiredEntityRow;
		}
		
		CcpJsonRepresentation requiredEntityRow = this.entity.getRequiredEntityRow(unionAll, json);
		return requiredEntityRow;
	}
}

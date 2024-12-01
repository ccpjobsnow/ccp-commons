package com.ccp.especifications.db.utils.decorators;

import java.util.List;
import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.bulk.CcpBulkItem;
import com.ccp.especifications.db.bulk.CcpEntityOperationType;
import com.ccp.especifications.db.crud.CcpSelectUnionAll;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpEntityField;
import com.ccp.exceptions.process.CcpFlow;
import com.ccp.process.CcpProcessStatus;

 class CopyableEntity implements CcpEntity{
	
	final CcpEntity entity;
	
	public CopyableEntity(CcpEntity entity) {
		this.entity = entity;
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

	public List<CcpJsonRepresentation> getParametersToSearch(CcpJsonRepresentation json) {
		return this.entity.getParametersToSearch(json);
	}

	public boolean isPresentInThisUnionAll(CcpSelectUnionAll unionAll, CcpJsonRepresentation json) {
		return this.entity.isPresentInThisUnionAll(unionAll, json);
	}

	public CcpJsonRepresentation getRecordFromUnionAll(CcpSelectUnionAll unionAll, CcpJsonRepresentation json) {
		return this.entity.getRecordFromUnionAll(unionAll, json);
	}

	public String getEntityName() {
		return this.entity.getEntityName();
	}

	public String calculateId(CcpJsonRepresentation json) {
		return this.entity.calculateId(json);
	}

	public CcpJsonRepresentation getPrimaryKeyValues(CcpJsonRepresentation json) {
		return this.entity.getPrimaryKeyValues(json);
	}

	public CcpBulkItem getRecordCopyToBulkOperation(CcpJsonRepresentation json, CcpEntityOperationType operation) {
		return this.entity.getRecordCopyToBulkOperation(json, operation);
	}

	public CcpEntityField[] getFields() {
		return this.entity.getFields();
	}

	public boolean isCopyableEntity() {
		return this.entity.isCopyableEntity();
	}

	public CcpBulkItem toBulkItem(CcpJsonRepresentation json, CcpEntityOperationType operation) {
		return this.entity.toBulkItem(json, operation);
	}

	public CcpJsonRepresentation getOneById(String id) {
		return this.entity.getOneById(id);
	}

	public boolean exists(String id) {
		return this.entity.exists(id);
	}

	public boolean exists(CcpJsonRepresentation json) {
		return this.entity.exists(json);
	}

	public CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation json) {
		return this.entity.createOrUpdate(json);
	}

	public boolean create(CcpJsonRepresentation json) {
		return this.entity.create(json);
	}

	public CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation json, String id) {
		return this.entity.createOrUpdate(json, id);
	}

	public CcpJsonRepresentation getOnlyExistingFields(CcpJsonRepresentation json) {
		return this.entity.getOnlyExistingFields(json);
	}

	public List<String> getPrimaryKeyNames() {
		return this.entity.getPrimaryKeyNames();
	}

	public boolean isVirtualEntity() {
		return this.entity.isVirtualEntity();
	}

	public boolean isPresentInThisJsonInMainEntity(CcpJsonRepresentation json) {
		return this.entity.isPresentInThisJsonInMainEntity(json);
	}

	public CcpJsonRepresentation getInnerJsonFromMainAndTwinEntities(CcpJsonRepresentation json) {
		return this.entity.getInnerJsonFromMainAndTwinEntities(json);
	}

	public CcpJsonRepresentation getData(CcpJsonRepresentation json) {
		return this.entity.getData(json);
	}

	public String[] getEntitiesToSelect() {
		return this.entity.getEntitiesToSelect();
	}


	
}

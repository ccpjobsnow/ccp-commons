package com.ccp.especifications.db.utils.decorators;

import java.util.List;
import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.bulk.CcpBulkItem;
import com.ccp.especifications.db.bulk.CcpEntityOperationType;
import com.ccp.especifications.db.crud.CcpSelectUnionAll;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpEntityField;

class TwinEntity implements CcpEntity {

	private final String twinEntityName;
	private final CcpEntity parent;
	private final CcpEntity entity;

	public TwinEntity(String twinEntityName, CcpEntity parent, CcpEntity entity) {
		this.twinEntityName = twinEntityName;
		this.entity = entity;
		this.parent = parent;
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
		return this.twinEntityName;
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

	public boolean hasTwinEntity() {
		return this.entity.hasTwinEntity();
	}

	public CcpBulkItem toBulkItem(CcpJsonRepresentation json, CcpEntityOperationType operation) {
		return this.entity.toBulkItem(json, operation);
	}

	public CcpEntity getTwinEntity() {
		return this.parent;
	}

	public CcpJsonRepresentation getOneById(CcpJsonRepresentation json,
			Function<CcpJsonRepresentation, CcpJsonRepresentation> ifNotFound) {
		return this.entity.getOneById(json, ifNotFound);
	}

	public CcpJsonRepresentation getOneById(CcpJsonRepresentation json) {
		return this.entity.getOneById(json);
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

	public boolean delete(CcpJsonRepresentation json) {
		return this.entity.delete(json);
	}

	public boolean delete(String id) {
		return this.entity.delete(id);
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

	public CcpJsonRepresentation getRequiredEntityRow(CcpSelectUnionAll unionAll, CcpJsonRepresentation json) {
		return this.entity.getRequiredEntityRow(unionAll, json);
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

package com.ccp.especifications.db.utils.decorators;

import java.util.List;
import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.bulk.CcpBulkItem;
import com.ccp.especifications.db.bulk.CcpEntityOperationType;
import com.ccp.especifications.db.crud.CcpSelectUnionAll;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpEntityField;

class ReplicableEntity implements  CcpEntity{
	private final String twinEntityName;
	private final CcpEntity entity;
	public ReplicableEntity(String twinEntityName, CcpEntity entity) {
		this.twinEntityName = twinEntityName;
		this.entity = entity;
	}
	public List<CcpJsonRepresentation> getParametersToSearch(CcpJsonRepresentation json) {
		return entity.getParametersToSearch(json);
	}
	public boolean isPresentInThisUnionAll(CcpSelectUnionAll unionAll, CcpJsonRepresentation json) {
		return entity.isPresentInThisUnionAll(unionAll, json);
	}
	public CcpJsonRepresentation getRecordFromUnionAll(CcpSelectUnionAll unionAll, CcpJsonRepresentation json) {
		return entity.getRecordFromUnionAll(unionAll, json);
	}
	public String getEntityName() {
		return entity.getEntityName();
	}
	public String calculateId(CcpJsonRepresentation json) {
		return entity.calculateId(json);
	}
	public CcpJsonRepresentation getPrimaryKeyValues(CcpJsonRepresentation json) {
		return entity.getPrimaryKeyValues(json);
	}
	public CcpBulkItem getRecordCopyToBulkOperation(CcpJsonRepresentation json, CcpEntityOperationType operation) {
		return entity.getRecordCopyToBulkOperation(json, operation);
	}
	public CcpEntityField[] getFields() {
		return entity.getFields();
	}
	public boolean isCopyableEntity() {
		return entity.isCopyableEntity();
	}
	public boolean hasTwinEntity() {
		return entity.hasTwinEntity();
	}
	public CcpBulkItem toBulkItem(CcpJsonRepresentation json, CcpEntityOperationType operation) {
		return entity.toBulkItem(json, operation);
	}
	
	public final CcpEntity getTwinEntity() {
		CopyableEntity parent = new CopyableEntity(this);
		TwinEntity twinEntity = new TwinEntity(this.twinEntityName, parent, this.entity);
		return twinEntity;
	}
	public CcpJsonRepresentation getOneById(CcpJsonRepresentation json,
			Function<CcpJsonRepresentation, CcpJsonRepresentation> ifNotFound) {
		return entity.getOneById(json, ifNotFound);
	}
	public CcpJsonRepresentation getOneById(CcpJsonRepresentation json) {
		return entity.getOneById(json);
	}
	public CcpJsonRepresentation getOneById(String id) {
		return entity.getOneById(id);
	}
	public boolean exists(String id) {
		return entity.exists(id);
	}
	public boolean exists(CcpJsonRepresentation json) {
		return entity.exists(json);
	}
	public CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation json) {
		return entity.createOrUpdate(json);
	}
	public boolean create(CcpJsonRepresentation json) {
		return entity.create(json);
	}
	public boolean delete(CcpJsonRepresentation json) {
		return entity.delete(json);
	}
	public boolean delete(String id) {
		return entity.delete(id);
	}
	public CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation json, String id) {
		return entity.createOrUpdate(json, id);
	}
	public CcpJsonRepresentation getOnlyExistingFields(CcpJsonRepresentation json) {
		return entity.getOnlyExistingFields(json);
	}
	public List<String> getPrimaryKeyNames() {
		return entity.getPrimaryKeyNames();
	}
	public boolean isVirtualEntity() {
		return entity.isVirtualEntity();
	}
	public CcpJsonRepresentation getRequiredEntityRow(CcpSelectUnionAll unionAll, CcpJsonRepresentation json) {
		return entity.getRequiredEntityRow(unionAll, json);
	}
	public boolean isPresentInThisJsonInMainEntity(CcpJsonRepresentation json) {
		return entity.isPresentInThisJsonInMainEntity(json);
	}
	public CcpJsonRepresentation getInnerJsonFromMainAndTwinEntities(CcpJsonRepresentation json) {
		return entity.getInnerJsonFromMainAndTwinEntities(json);
	}
	public CcpJsonRepresentation getData(CcpJsonRepresentation json) {
		return entity.getData(json);
	}
	public String[] getEntitiesToSelect() {
		return entity.getEntitiesToSelect();
	}

	
	
}

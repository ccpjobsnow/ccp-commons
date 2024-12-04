package com.ccp.especifications.db.utils.decorators;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.cache.CcpCacheDecorator;
import com.ccp.especifications.db.bulk.CcpBulkItem;
import com.ccp.especifications.db.bulk.CcpEntityOperationType;
import com.ccp.especifications.db.crud.CcpSelectUnionAll;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpEntityField;

public abstract class CcpEntityDelegator implements CcpEntity{

	protected final CcpEntity entity;

	public CcpEntityDelegator(CcpEntity entity) {
		this.entity = entity;
	}

	public List<CcpJsonRepresentation> getParametersToSearch(CcpJsonRepresentation json) {
		List<CcpJsonRepresentation> parametersToSearch = this.entity.getParametersToSearch(json);
		return parametersToSearch;
	}

	public boolean isPresentInThisUnionAll(CcpSelectUnionAll unionAll, CcpJsonRepresentation json) {
		boolean presentInThisUnionAll = this.entity.isPresentInThisUnionAll(unionAll, json);
		return presentInThisUnionAll;
	}

	public CcpJsonRepresentation getRecordFromUnionAll(CcpSelectUnionAll unionAll, CcpJsonRepresentation json) {
		CcpJsonRepresentation recordFromUnionAll = this.entity.getRecordFromUnionAll(unionAll, json);
		return recordFromUnionAll;
	}

	public String getEntityName() {
		String entityName = this.entity.getEntityName();
		return entityName;
	}

	public String calculateId(CcpJsonRepresentation json) {
		String calculateId = this.entity.calculateId(json);
		return calculateId;
	}

	public CcpJsonRepresentation getPrimaryKeyValues(CcpJsonRepresentation json) {
		CcpJsonRepresentation primaryKeyValues = this.entity.getPrimaryKeyValues(json);
		return primaryKeyValues;
	}

	public CcpBulkItem getRecordCopyToBulkOperation(CcpJsonRepresentation json, CcpEntityOperationType operation) {
		CcpBulkItem recordCopyToBulkOperation = this.entity.getRecordCopyToBulkOperation(json, operation);
		return recordCopyToBulkOperation;
	}

	public CcpEntityField[] getFields() {
		CcpEntityField[] fields = this.entity.getFields();
		return fields;
	}

	public boolean isCopyableEntity() {
		boolean copyableEntity = this.entity.isCopyableEntity();
		return copyableEntity;
	}

	public CcpBulkItem toBulkItem(CcpJsonRepresentation json, CcpEntityOperationType operation) {
		CcpBulkItem bulkItem = this.entity.toBulkItem(json, operation);
		return bulkItem;
	}

	public CcpEntity getTwinEntity() {
		CcpEntity twinEntity = this.entity.getTwinEntity();
		return twinEntity;
	}

	public CcpJsonRepresentation getOneById(CcpJsonRepresentation json,
			Function<CcpJsonRepresentation, CcpJsonRepresentation> ifNotFound) {
		CcpJsonRepresentation oneById = this.entity.getOneById(json, ifNotFound);
		return oneById;
	}

	public CcpJsonRepresentation getOneById(CcpJsonRepresentation json) {
		CcpJsonRepresentation oneById = this.entity.getOneById(json);
		return oneById;
	}

	public CcpJsonRepresentation getOneById(String id) {
		CcpJsonRepresentation oneById = this.entity.getOneById(id);
		return oneById;
	}

	public boolean exists(String id) {
		boolean exists = this.entity.exists(id);
		return exists;
	}

	public boolean exists(CcpJsonRepresentation json) {
		boolean exists = this.entity.exists(json);
		return exists;
	}

	public CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation json) {
		CcpJsonRepresentation createOrUpdate = this.entity.createOrUpdate(json);
		return createOrUpdate;
	}

	public boolean create(CcpJsonRepresentation json) {
		boolean create = this.entity.create(json);
		return create;
	}

	public boolean delete(CcpJsonRepresentation json) {
		boolean delete = this.entity.delete(json);
		return delete;
	}

	public boolean delete(String id) {
		boolean delete = this.entity.delete(id);
		return delete;
	}

	public CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation json, String id) {
		CcpJsonRepresentation createOrUpdate = this.entity.createOrUpdate(json, id);
		return createOrUpdate;
	}

	public CcpJsonRepresentation getOnlyExistingFields(CcpJsonRepresentation json) {
		CcpJsonRepresentation onlyExistingFields = this.entity.getOnlyExistingFields(json);
		return onlyExistingFields;
	}

	public List<String> getPrimaryKeyNames() {
		List<String> primaryKeyNames = this.entity.getPrimaryKeyNames();
		return primaryKeyNames;
	}

	public CcpJsonRepresentation getRequiredEntityRow(CcpSelectUnionAll unionAll, CcpJsonRepresentation json) {
		CcpJsonRepresentation requiredEntityRow = this.entity.getRequiredEntityRow(unionAll, json);
		return requiredEntityRow;
	}

	public boolean isPresentInThisJsonInMainEntity(CcpJsonRepresentation json) {
		boolean presentInThisJsonInMainEntity = this.entity.isPresentInThisJsonInMainEntity(json);
		return presentInThisJsonInMainEntity;
	}

	public CcpJsonRepresentation getInnerJsonFromMainAndTwinEntities(CcpJsonRepresentation json) {
		CcpJsonRepresentation innerJsonFromMainAndTwinEntities = this.entity.getInnerJsonFromMainAndTwinEntities(json);
		return innerJsonFromMainAndTwinEntities;
	}

	public CcpJsonRepresentation getData(CcpJsonRepresentation json) {
		CcpJsonRepresentation data = this.entity.getData(json);
		return data;
	}

	public String[] getEntitiesToSelect() {
		String[] entitiesToSelect = this.entity.getEntitiesToSelect();
		return entitiesToSelect;
	}

	public ArrayList<Object> getSortedPrimaryKeyValues(CcpJsonRepresentation json) {
		ArrayList<Object> sortedPrimaryKeyValues = this.entity.getSortedPrimaryKeyValues(json);
		return sortedPrimaryKeyValues;
	}

	public CcpCacheDecorator getCache(String id) {
		CcpCacheDecorator cache = this.entity.getCache(id);
		return cache;
	}

	public CcpCacheDecorator getCache(CcpJsonRepresentation json) {
		CcpCacheDecorator cache = this.entity.getCache(json);
		return cache;
	}
	
}
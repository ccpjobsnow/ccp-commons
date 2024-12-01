package com.ccp.especifications.db.utils.decorators;

import java.util.List;
import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.cache.CcpCacheDecorator;
import com.ccp.especifications.db.bulk.CcpBulkItem;
import com.ccp.especifications.db.bulk.CcpEntityOperationType;
import com.ccp.especifications.db.crud.CcpSelectUnionAll;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpEntityField;

public class CacheEntity implements CcpEntity {

	private final int cacheExpires;
	private final CcpEntity entity;

	public CacheEntity(CcpEntity entity, int cacheExpires) {
		this.cacheExpires = cacheExpires;
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
		boolean canSaveCopy = this.entity.isCopyableEntity();
		return canSaveCopy;
	}

	public boolean hasTwinEntity() {
		boolean hasMirrorEntity = this.entity.hasTwinEntity();
		return hasMirrorEntity;
	}

	public CcpBulkItem toBulkItem(CcpJsonRepresentation json, CcpEntityOperationType operation) {
		CcpBulkItem bulkItem = this.entity.toBulkItem(json, operation);
		return bulkItem;
	}

	public CcpEntity getTwinEntity() {
		return this.entity.getTwinEntity();
	}

	
	public CcpJsonRepresentation getOneById(CcpJsonRepresentation json,
			Function<CcpJsonRepresentation, CcpJsonRepresentation> ifNotFound) {
		
		CcpCacheDecorator cache = this.getCache(json);
	
		CcpJsonRepresentation result = cache.get(x -> this.entity.getOneById(json, ifNotFound), this.cacheExpires);
		
		return result;
	}

	public CcpJsonRepresentation getOneById(CcpJsonRepresentation json) {
		
		CcpCacheDecorator cache = this.getCache(json);
		
		CcpJsonRepresentation result = cache.get(x -> this.entity.getOneById(json), this.cacheExpires);
		
		return result;
	}

	public CcpJsonRepresentation getOneById(String id) {

		CcpCacheDecorator cache = this.getCache(id);
		
		CcpJsonRepresentation result = cache.get(x -> this.entity.getOneById(id), this.cacheExpires);
		
		return result;
	}

	public boolean exists(String id) {

		CcpCacheDecorator cache = this.getCache(id);

		boolean exists = cache.isPresentInTheCache();
		
		if(exists) {
			return true;
		}

		CcpJsonRepresentation result = cache.get(x -> this.entity.getOneById(id), this.cacheExpires);
		
		boolean found = result.isEmpty() == false;
		
		if(found) {
			return true;
		}
		
		cache.delete();
		
		return false;
	}

	public boolean exists(CcpJsonRepresentation json) {
		
		String id = this.calculateId(json);
		
		boolean exists = this.exists(id);
		
		return exists;
	}

	public CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation json) {

		CcpJsonRepresentation createOrUpdate = this.entity.createOrUpdate(json);
		
		CcpCacheDecorator cache = this.getCache(json);
		
		cache.put(createOrUpdate, this.cacheExpires);
		
		return createOrUpdate;
	}

	public boolean create(CcpJsonRepresentation json) {
		
		boolean create = this.entity.create(json);
		
		CcpCacheDecorator cache = this.getCache(json);
		
		cache.put(json, this.cacheExpires);
		
		return create;
	}

	public boolean delete(CcpJsonRepresentation json) {
		
		boolean delete = this.entity.delete(json);
		
		CcpCacheDecorator cache = this.getCache(json);
		
		cache.delete();
		
		return delete;
	}

	public boolean delete(String id) {
		
		boolean delete = this.entity.delete(id);
		
		CcpCacheDecorator cache = this.getCache(id);
		
		cache.delete();
		
		return delete;
	}

	public CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation json, String id) {

		CcpJsonRepresentation createOrUpdate = this.entity.createOrUpdate(json, id);
		
		CcpCacheDecorator cache = this.getCache(json);
		
		cache.put(createOrUpdate, this.cacheExpires);
		
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

	public CcpEntity fromCache() {
		throw new UnsupportedOperationException();
	}
	
	private CcpCacheDecorator getCache(CcpJsonRepresentation json) {
		
		String id = this.calculateId(json);
		
		CcpCacheDecorator cache = this.getCache(id);
		
		return cache;
	}
	
	private CcpCacheDecorator getCache(String id) {
		String entityName = this.getEntityName();
		
		CcpCacheDecorator cache = new CcpCacheDecorator("records")
				.incrementKey("entity", entityName)	
				.incrementKey("id", id)	
				;
		return cache;
	}

}
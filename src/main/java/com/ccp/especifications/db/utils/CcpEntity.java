package com.ccp.especifications.db.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpHashDecorator;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.cache.CcpCacheDecorator;
import com.ccp.especifications.db.bulk.CcpBulkItem;
import com.ccp.especifications.db.bulk.CcpEntityOperationType;
import com.ccp.especifications.db.crud.CcpCrud;
import com.ccp.especifications.db.crud.CcpSelectUnionAll;
import com.ccp.exceptions.db.CcpEntityRecordNotFound;
import com.ccp.exceptions.process.CcpFlow;
import com.ccp.process.CcpProcessStatus;


public interface CcpEntity{

	List<CcpJsonRepresentation> getParametersToSearch(CcpJsonRepresentation json);
	
	boolean isPresentInThisUnionAll(CcpSelectUnionAll unionAll, CcpJsonRepresentation json);
	
	CcpJsonRepresentation getRecordFromUnionAll(CcpSelectUnionAll unionAll, CcpJsonRepresentation json);

	String getEntityName();

	default String calculateId(CcpJsonRepresentation json) {
		List<String> primaryKeyNames = this.getPrimaryKeyNames();
		
		boolean hasNoPrimaryKey = primaryKeyNames.isEmpty();
		
		if(hasNoPrimaryKey) {
			String string = UUID.randomUUID().toString();
			String hash = new CcpStringDecorator(string).hash().asString("SHA1");
			return hash;
		}
		
		ArrayList<Object> sortedPrimaryKeyValues = this.getSortedPrimaryKeyValues(json);
		
		String replace = sortedPrimaryKeyValues.toString().replace("[", "").replace("]", "");
		CcpHashDecorator hash2 = new CcpStringDecorator(replace).hash();
		String hash = hash2.asString("SHA1");
		return hash;
	}
	
	default CcpJsonRepresentation getPrimaryKeyValues(CcpJsonRepresentation json) {
		
		List<String> onlyPrimaryKey = this.getPrimaryKeyNames();
		CcpJsonRepresentation jsonPiece = json.getJsonPiece(onlyPrimaryKey);
		return jsonPiece;
	}
	
	CcpBulkItem getRecordCopyToBulkOperation(CcpJsonRepresentation json, CcpEntityOperationType operation);
	
	CcpEntityField[] getFields();
	
	default boolean isCopyableEntity() {
		
		List<String> primaryKeyNames = this.getPrimaryKeyNames();
		int primaryKeyFieldsSize = primaryKeyNames.size();
		CcpEntityField[] fields = this.getFields();
		boolean thisEntityHasMoreFieldsBesidesPrimaryKeys = primaryKeyFieldsSize < fields.length;
		return thisEntityHasMoreFieldsBesidesPrimaryKeys;
	}
	
	default CcpBulkItem toBulkItem(CcpJsonRepresentation json, CcpEntityOperationType operation) {
		CcpBulkItem ccpBulkItem = new CcpBulkItem(json, operation, this);
		return ccpBulkItem;
	}
	
	default CcpEntity getTwinEntity() {
		throw new UnsupportedOperationException();
	}

	default CcpJsonRepresentation getOneById(CcpJsonRepresentation json, Function<CcpJsonRepresentation, CcpJsonRepresentation> ifNotFound) {
		try {
			CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
			CcpJsonRepresentation oneById = crud.getOneById(this, json);
			return oneById;
			
		} catch (CcpEntityRecordNotFound e) {
			CcpJsonRepresentation execute = ifNotFound.apply(json);
			return execute;
		}
	}

	default CcpJsonRepresentation getOneById(CcpJsonRepresentation json) {
		String entityName = this.getEntityName();
		CcpJsonRepresentation md = this.getOneById(json, x -> {throw new CcpFlow(x.put("entity", entityName), CcpProcessStatus.NOT_FOUND);});
		return md;
	}
	

	default CcpJsonRepresentation getOneById(String id) {
		try {
			CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
			CcpJsonRepresentation md = crud.getOneById(this, id);
			return md;
			
		} catch (CcpEntityRecordNotFound e) {
			String entityName = this.getEntityName();
			CcpJsonRepresentation put = CcpConstants.EMPTY_JSON.put("id", id).put("entity", entityName);
			throw new CcpFlow(put, CcpProcessStatus.NOT_FOUND);
		}
	}
	
	default boolean exists(String id) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		boolean exists = crud.exists(this, id);
		return exists;
		
	}
	
	default boolean exists(CcpJsonRepresentation json) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		boolean exists = crud.exists(this, json);
		return exists;
	}
	
	
	default CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation json) {
		CcpJsonRepresentation onlyExistingFields = this.getOnlyExistingFields(json);
		this.create(json);
		return onlyExistingFields;
	}

	default boolean create(CcpJsonRepresentation json) {
		CcpJsonRepresentation onlyExistingFields = this.getOnlyExistingFields(json);
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		
		CcpJsonRepresentation createOrUpdate = crud.createOrUpdate(this, onlyExistingFields);
		String result = createOrUpdate.getAsString("result");
		boolean created = "created".equals(result);
		
		return created;
	}
	

	default boolean delete(CcpJsonRepresentation json) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		boolean remove = crud.delete(this, json);
		return remove;
	}
	
	default boolean delete(String id) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		boolean remove = crud.delete(this, id);
		return remove;
	}

	
	default CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation json, String id) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		CcpJsonRepresentation createOrUpdate = crud.createOrUpdate(this, json, id);
		return createOrUpdate;
	}
	

	default CcpJsonRepresentation getOnlyExistingFields(CcpJsonRepresentation json) {
		CcpEntityField[] fields = this.getFields();
		String[] array = Arrays.asList(fields).stream().map(x -> x.name()).collect(Collectors.toList()).toArray(new String[fields.length]);
		CcpJsonRepresentation subMap = json.getJsonPiece(array);
		return subMap;
	}

	default List<String> getPrimaryKeyNames() {
		CcpEntityField[] fields = this.getFields();
		List<String> onlyPrimaryKey = new ArrayList<>(Arrays.asList(fields).stream().filter(x -> x.isPrimaryKey()).map(x -> x.name()).collect(Collectors.toList()));
		return onlyPrimaryKey;
	}
	
	default CcpJsonRepresentation getRequiredEntityRow(CcpSelectUnionAll unionAll, CcpJsonRepresentation json) {
		
		boolean notFound = this.isPresentInThisUnionAll(unionAll, json) == false;

		if(notFound) {
			throw new CcpEntityRecordNotFound(this, json);
		}
		
		CcpJsonRepresentation entityRow = this.getRecordFromUnionAll(unionAll, json);
		
		return entityRow;
	}

	default boolean isPresentInThisJsonInMainEntity(CcpJsonRepresentation json) {
		CcpJsonRepresentation innerJsonFromPath = json.getInnerJsonFromPath("_entities", this.getEntityName());
		return innerJsonFromPath.isEmpty() == false;
	}

	default CcpJsonRepresentation getInnerJsonFromMainAndTwinEntities(CcpJsonRepresentation json) {
		String entityName = this.getEntityName();
		CcpEntity twinEntity = this.getTwinEntity();
		String twinEntityName = twinEntity.getEntityName();
		CcpJsonRepresentation j1 = json.getInnerJsonFromPath("_entities", entityName);
		CcpJsonRepresentation j2 = json.getInnerJsonFromPath("_entities", twinEntityName);
		CcpJsonRepresentation putAll = j1.putAll(j2);
		return putAll;
	}
	
	default CcpJsonRepresentation getData(CcpJsonRepresentation json) {
		
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
	
	default String[] getEntitiesToSelect() {
		String entityName = this.getEntityName();
		String[] resourcesNames = new String[]{entityName};
		return resourcesNames;
	}
	default ArrayList<Object> getSortedPrimaryKeyValues(CcpJsonRepresentation json) {
		
		CcpJsonRepresentation primaryKeyValues = this.getPrimaryKeyValues(json);
		
		List<String> primaryKeyNames = this.getPrimaryKeyNames();
		
		Set<String> missingKeys = primaryKeyValues.getMissingFields(primaryKeyNames);

		boolean isMissingKeys = missingKeys.isEmpty() == false;
		
		if(isMissingKeys) {
			throw new RuntimeException("It is missing the keys '" + missingKeys + "' from entity '" + this + "' in the object " + json );
		}
		
		TreeMap<String, Object> treeMap = new TreeMap<>(primaryKeyValues.content);
		Collection<Object> values2 = treeMap.values();
		ArrayList<Object> onlyPrimaryKeys = new ArrayList<>(values2);
		return onlyPrimaryKeys;
	}
	default CcpCacheDecorator getCache(String id) {
		String entityName = this.getEntityName();
		
		CcpCacheDecorator cache = new CcpCacheDecorator("records")
				.incrementKey("entity", entityName)	
				.incrementKey("id", id)	
				;
		return cache;
	}
	default CcpCacheDecorator getCache(CcpJsonRepresentation json) {
		
		String id = this.calculateId(json);
		
		CcpCacheDecorator cache = this.getCache(id);
		
		return cache;
	}
	
	default CcpEntity[] getThisEntityAndHisTwinEntity() {
		return new CcpEntity[] {this};
	}

}

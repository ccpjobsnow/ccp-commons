package com.ccp.especifications.db.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ccp.constantes.CcpOtherConstants;
import com.ccp.decorators.CcpHashDecorator;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.db.bulk.CcpBulkItem;
import com.ccp.especifications.db.bulk.CcpEntityOperationType;
import com.ccp.especifications.db.crud.CcpCrud;
import com.ccp.especifications.db.crud.CcpSelectUnionAll;
import com.ccp.exceptions.db.CcpEntityRecordNotFound;
import com.ccp.exceptions.process.CcpFlowDiversion;
import com.ccp.process.CcpDefaultProcessStatus;
import com.ccp.utils.CcpHashAlgorithm;


public interface CcpEntity{

	default CcpBulkItem toBulkItemToCreateOrDelete(CcpSelectUnionAll unionAll, CcpJsonRepresentation json) {
		boolean presentInThisUnionAll = this.isPresentInThisUnionAll(unionAll, json);
		CcpEntityOperationType operation = presentInThisUnionAll ? CcpEntityOperationType.delete : CcpEntityOperationType.create;
		CcpBulkItem bulkItem = this.toBulkItem(json, operation);
		return bulkItem;
	}
	
	default List<CcpJsonRepresentation> getParametersToSearch(CcpJsonRepresentation json) {
		
		String id = this.calculateId(json);

		CcpDbRequester dependency = CcpDependencyInjection.getDependency(CcpDbRequester.class);
		
		String fieldNameToEntity = dependency.getFieldNameToEntity();
		String fieldNameToId = dependency.getFieldNameToId();
		
		String entityName = this.getEntityName();
		
		CcpJsonRepresentation mainRecord = CcpOtherConstants.EMPTY_JSON
		.put(fieldNameToEntity, entityName)
		.put(fieldNameToId, id)
		;
		List<CcpJsonRepresentation> asList = Arrays.asList(mainRecord);
		return asList;
	}

	default boolean isPresentInThisUnionAll(CcpSelectUnionAll unionAll, CcpJsonRepresentation json) {
		
		String index = this.getEntityName();
		String id = this.calculateId(json);

		boolean present = unionAll.isPresent(index, id);
		
		return present;
	}
	

	String getEntityName();

	default String calculateId(CcpJsonRepresentation json) {
		List<String> primaryKeyNames = this.getPrimaryKeyNames();
		
		boolean hasNoPrimaryKey = primaryKeyNames.isEmpty();
		
		if(hasNoPrimaryKey) {
			String string = UUID.randomUUID().toString();
			String hash = new CcpStringDecorator(string).hash().asString(CcpHashAlgorithm.SHA1);
			return hash;
		}
		
		ArrayList<Object> sortedPrimaryKeyValues = this.getSortedPrimaryKeyValues(json);
		
		String replace = sortedPrimaryKeyValues.toString().replace("[", "").replace("]", "");
		CcpHashDecorator hash2 = new CcpStringDecorator(replace).hash();
		String hash = hash2.asString(CcpHashAlgorithm.SHA1);
		return hash;
	}
	
	default CcpJsonRepresentation getPrimaryKeyValues(CcpJsonRepresentation json) {
		
		List<String> onlyPrimaryKey = this.getPrimaryKeyNames();
		List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> jsonTransformers = this.getJsonTransformers();
		CcpJsonRepresentation transformedJson = json.getTransformedJson(jsonTransformers);
		CcpJsonRepresentation jsonPiece = transformedJson.getJsonPiece(onlyPrimaryKey);
		return jsonPiece;
	}
	
	default CcpBulkItem getRecordCopyToBulkOperation(CcpJsonRepresentation json, CcpEntityOperationType operation) {
		throw new UnsupportedOperationException();
	}
	
	CcpEntityField[] getFields();
	
	default boolean isCopyableEntity() {
		
		List<String> primaryKeyNames = this.getPrimaryKeyNames();
		int primaryKeyFieldsSize = primaryKeyNames.size();
		CcpEntityField[] fields = this.getFields();
		boolean thisEntityHasMoreFieldsBesidesPrimaryKeys = primaryKeyFieldsSize < fields.length;
		return thisEntityHasMoreFieldsBesidesPrimaryKeys;
	}
	
	default CcpBulkItem toBulkItem(CcpJsonRepresentation json, CcpEntityOperationType operation) {
		String calculateId = this.calculateId(json);
		CcpBulkItem ccpBulkItem = new CcpBulkItem(json, operation, this, calculateId);
		return ccpBulkItem;
	}
	
	default CcpEntity getTwinEntity() {
		throw new UnsupportedOperationException();
	}

	default CcpJsonRepresentation getOneById(CcpJsonRepresentation json, Function<CcpJsonRepresentation, CcpJsonRepresentation> ifNotFound) {
		try {
			CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
			String calculateId = this.calculateId(json);
			String entityName = this.getEntityName();
			CcpJsonRepresentation oneById = crud.getOneById(entityName, calculateId);
			return oneById;
			
		} catch (CcpEntityRecordNotFound e) {
			CcpJsonRepresentation execute = ifNotFound.apply(json);
			return execute;
		}
	}

	default CcpJsonRepresentation getOneById(CcpJsonRepresentation json) {
		String entityName = this.getEntityName();
		CcpJsonRepresentation md = this.getOneById(json, x -> {throw new CcpFlowDiversion(x.put("entity", entityName), CcpDefaultProcessStatus.NOT_FOUND);});
		return md;
	}
	

	default CcpJsonRepresentation getOneById(String id) {
		try {
			CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
			String entityName = this.getEntityName();
			CcpJsonRepresentation md = crud.getOneById(entityName, id);
			return md;
			
		} catch (CcpEntityRecordNotFound e) {
			String entityName = this.getEntityName();
			CcpJsonRepresentation put = CcpOtherConstants.EMPTY_JSON.put("id", id).put("entity", entityName);
			throw new CcpFlowDiversion(put, CcpDefaultProcessStatus.NOT_FOUND);
		}
	}
	
	default boolean exists(String id) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		String entityName = this.getEntityName();
		boolean exists = crud.exists(entityName, id);
		return exists;
		
	}
	
	default boolean exists(CcpJsonRepresentation json) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		String id = this.calculateId(json);
		String entityName = this.getEntityName();
		boolean exists = crud.exists(entityName, id);
		return exists;
	}
	
	default CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation json) {
		this.create(json);
		List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> jsonTransformers = this.getJsonTransformers();
		CcpJsonRepresentation handledJson = json.getTransformedJson(jsonTransformers);
		return handledJson;
	}

	default CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation json, String id) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		String entityName = this.getEntityName();
		CcpJsonRepresentation handledJson = this.getHandledJson(json);
		CcpJsonRepresentation onlyExistingFields = this.getOnlyExistingFields(handledJson);
		crud.createOrUpdate(entityName, onlyExistingFields, id);
		return handledJson;
	}

	default boolean create(CcpJsonRepresentation json) {
		CcpJsonRepresentation handledJson = this.getHandledJson(json);
		CcpJsonRepresentation onlyExistingFields = this.getOnlyExistingFields(handledJson);
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		String calculateId = this.calculateId(json);
		String entityName = this.getEntityName();
		CcpJsonRepresentation createOrUpdate = crud.createOrUpdate(entityName, onlyExistingFields, calculateId);
		String result = createOrUpdate.getAsString("result");
		boolean created = "created".equals(result);
		
		return created;
	}
	
	default boolean create(CcpJsonRepresentation json, String calculateId) {
		CcpJsonRepresentation handledJson = this.getHandledJson(json);
		CcpJsonRepresentation onlyExistingFields = this.getOnlyExistingFields(handledJson);
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		String entityName = this.getEntityName();
		CcpJsonRepresentation createOrUpdate = crud.createOrUpdate(entityName, onlyExistingFields, calculateId);
		String result = createOrUpdate.getAsString("result");
		boolean created = "created".equals(result);
		
		return created;
	}
	

	default CcpJsonRepresentation delete(CcpJsonRepresentation json) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		String calculateId = this.calculateId(json);
		String entityName = this.getEntityName();
		crud.delete(entityName, calculateId);
		return json;
	}
	
	default boolean delete(String id) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		String entityName = this.getEntityName();
		boolean remove = crud.delete(entityName, id);
		return remove;
	}

	default CcpJsonRepresentation getHandledJson(CcpJsonRepresentation json) {
		List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> jsonTransformers = this.getJsonTransformers();
		CcpJsonRepresentation transformedJson = json.getTransformedJson(jsonTransformers);
		return transformedJson;
	}
	

	default CcpJsonRepresentation getOnlyExistingFields(CcpJsonRepresentation json) {
		CcpEntityField[] fields = this.getFields();
		String[] array = Arrays.asList(fields).stream().map(x -> x.name()).collect(Collectors.toList()).toArray(new String[fields.length]);
		CcpJsonRepresentation subMap = json.getJsonPiece(array);
		return subMap;
	}
	
	default CcpJsonRepresentation getOnlyExistingFieldsAndHandledJson(CcpJsonRepresentation json) {
		CcpJsonRepresentation handledJson = this.getHandledJson(json);
		CcpJsonRepresentation onlyExistingFields = this.getOnlyExistingFields(handledJson);
		return onlyExistingFields;
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
	default CcpJsonRepresentation getRecordFromUnionAll(CcpSelectUnionAll unionAll, CcpJsonRepresentation json) {

		String id = this.calculateId(json);
		String index = this.getEntityName();
		
		CcpJsonRepresentation jsonValue = unionAll.getEntityRow(index, id);
		
		return jsonValue;
	}

	default boolean isPresentInThisJsonInMainEntity(CcpJsonRepresentation json) {
		CcpJsonRepresentation innerJsonFromPath = json.getInnerJsonFromPath("_entities", this.getEntityName());
		return innerJsonFromPath.isEmpty() == false;
	}

	default CcpJsonRepresentation getInnerJsonFromMainAndTwinEntities(CcpJsonRepresentation json) {
		String entityName = this.getEntityName();
		CcpJsonRepresentation j1 = json.getInnerJsonFromPath("_entities", entityName);
		return j1;
	}
	
	default CcpJsonRepresentation getData(CcpJsonRepresentation json, Consumer<String[]> functionToDeleteKeysInTheCache) {
		
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		
		CcpSelectUnionAll searchResults = crud.unionBetweenMainAndTwinEntities(json, functionToDeleteKeysInTheCache, this);
		
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
	
	default CcpEntity[] getThisEntityAndHisTwinEntity() {
		return new CcpEntity[] {this};
	}

	
	default CcpEntity validateJson(CcpJsonRepresentation json) {
		return this;
	}
	
	List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> getJsonTransformers();
}

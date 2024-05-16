package com.ccp.especifications.db.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.db.bulk.CcpBulkItem;
import com.ccp.especifications.db.bulk.CcpEntityOperationType;
import com.ccp.especifications.db.crud.CcpCrud;
import com.ccp.exceptions.db.CcpEntityRecordNotFound;
import com.ccp.exceptions.process.CcpFlow;


public interface CcpEntity{

	String getEntityName();

	String getId(CcpJsonRepresentation values) ;
	
	CcpJsonRepresentation getPrimaryKeyValues(CcpJsonRepresentation values);
	
	CcpBulkItem getRecordToBulkOperation(CcpJsonRepresentation values, CcpEntityOperationType operation);
	
	CcpEntityField[] getFields();
	
	boolean canSaveCopy();
	
	boolean hasMirrorEntity();

	default CcpBulkItem toBulkItem(CcpJsonRepresentation json, CcpEntityOperationType operation) {
		CcpBulkItem ccpBulkItem = new CcpBulkItem(json, operation, this);
		return ccpBulkItem;
	}
	
	default CcpEntity getMirrorEntity() {
		return this;
	}

	default CcpJsonRepresentation getOneById(CcpJsonRepresentation data, Function<CcpJsonRepresentation, CcpJsonRepresentation> ifNotFound) {
		try {
			CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
			CcpJsonRepresentation oneById = crud.getOneById(this, data);
			return oneById;
			
		} catch (CcpEntityRecordNotFound e) {
			CcpJsonRepresentation execute = ifNotFound.apply(data);
			return execute;
		}
	}

	default CcpJsonRepresentation getOneById(CcpJsonRepresentation data) {
		String entityName = this.getEntityName();
		CcpJsonRepresentation md = this.getOneById(data, x -> {throw new CcpFlow(x.put("entity", entityName), 404);});
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
			throw new CcpFlow(put, 404);
		}
	}
	
	default boolean exists(String id) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		boolean exists = crud.exists(this, id);
		return exists;
		
	}
	
	default boolean exists(CcpJsonRepresentation data) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		boolean exists = crud.exists(this, data);
		return exists;
	}
	
	
	default CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation values) {
		CcpJsonRepresentation onlyExistingFields = this.getOnlyExistingFields(values);
		this.create(values);
		return onlyExistingFields;
	}

	default boolean create(CcpJsonRepresentation values) {
		CcpJsonRepresentation onlyExistingFields = this.getOnlyExistingFields(values);
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);

		CcpJsonRepresentation createOrUpdate = crud.createOrUpdate(this, onlyExistingFields);
		String result = createOrUpdate.getAsString("result");
		boolean created = "created".equals(result);
		
		return created;
	}
	

	default boolean delete(CcpJsonRepresentation values) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		boolean remove = crud.delete(this, values);
		return remove;
	}
	
	default boolean delete(String id) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		boolean remove = crud.delete(this, id);
		return remove;
	}

	
	default CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation data, String id) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		CcpJsonRepresentation createOrUpdate = crud.createOrUpdate(this, data, id);
		return createOrUpdate;
	}
	

	default List<CcpBulkItem> getFirstRecordsToInsert(){
		return new ArrayList<>();
	}
	
	
	default CcpJsonRepresentation getOnlyExistingFields(CcpJsonRepresentation values) {
		CcpEntityField[] fields = this.getFields();
		String[] array = Arrays.asList(fields).stream().map(x -> x.name()).collect(Collectors.toList()).toArray(new String[fields.length]);
		CcpJsonRepresentation subMap = values.getJsonPiece(array);
		return subMap;
	}
	
	default List<String> getPrimaryKeyNames() {
		CcpEntityField[] fields = this.getFields();
		List<String> onlyPrimaryKey = new ArrayList<>(Arrays.asList(fields).stream().filter(x -> x.isPrimaryKey()).map(x -> x.name()).collect(Collectors.toList()));
		return onlyPrimaryKey;
	}
	
}

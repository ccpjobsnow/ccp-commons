package com.ccp.especifications.db.utils.decorators;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.bulk.CcpBulkItem;
import com.ccp.especifications.db.bulk.CcpEntityOperationType;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpEntityField;
import com.ccp.validation.CcpJsonFieldsValidations;
import com.ccp.validation.annotations.CcpJsonFieldsValidation;

final class DefaultImplementationEntity implements CcpEntity{

	final String entityName;
	final Class<?> entityClass;
	final CcpEntityField[] fields;
	
	public DefaultImplementationEntity(String entityName, Class<?> entityClass, CcpEntityField... fields) {
		this.entityClass = entityClass;
		this.entityName = entityName;
		this.fields = fields;
	}

	public String getEntityName() {
		return this.entityName;
	}

	public final CcpEntityField[] getFields() {
		return this.fields;
	}

	public String toString() {
		String entityName = this.getEntityName();
		return entityName;
	}
	
	protected List<CcpBulkItem> toCreateBulkItems(String... jsons){
		List<CcpBulkItem> collect = Arrays.asList(jsons)
		.stream().map(x -> new CcpJsonRepresentation(x))
		.map(x -> new CcpBulkItem(x, CcpEntityOperationType.create, this))
		.collect(Collectors.toList());
		return collect;
	}
	
	public final int hashCode() {
		String entityName = this.getEntityName();
		return entityName.hashCode();
	}
	
	public final boolean equals(Object obj) {
		try {
			String entityName = ((DefaultImplementationEntity)obj).getEntityName();
			String entityName2 = this.getEntityName();
			boolean equals = entityName.equals(entityName2);
			return equals;
		} catch (Exception e) {
			return false;
		}
	}

	
	public boolean create(CcpJsonRepresentation json) {
		CcpJsonRepresentation addTimeFields = json.getTransformedJson(CcpAddTimeFields.INSTANCE);
		boolean create = CcpEntity.super.create(addTimeFields);
		return create;
	}
	
	public CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation json) {
		CcpJsonRepresentation addTimeFields = json.getTransformedJson(CcpAddTimeFields.INSTANCE);
		CcpJsonRepresentation createOrUpdate = CcpEntity.super.createOrUpdate(addTimeFields);
		return createOrUpdate;
	}
	
	public CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation json, String id) {
		CcpJsonRepresentation addTimeFields = json.getTransformedJson(CcpAddTimeFields.INSTANCE);
		CcpJsonRepresentation createOrUpdate = CcpEntity.super.createOrUpdate(addTimeFields, id);
		return createOrUpdate;
	}
	
	public CcpBulkItem toBulkItem(CcpJsonRepresentation json, CcpEntityOperationType operation) {
		CcpJsonRepresentation addTimeFields = json.getTransformedJson(CcpAddTimeFields.INSTANCE);
		CcpBulkItem bulkItem = CcpEntity.super.toBulkItem(addTimeFields, operation);
		return bulkItem;
	}
	
	public void validateJson(CcpJsonRepresentation json) {
		boolean hasNotAnnotation = this.entityClass.isAnnotationPresent(CcpJsonFieldsValidation.class) == false;
		
		if(hasNotAnnotation) {
			return;
		}
		
		CcpJsonFieldsValidation annotation = this.entityClass.getAnnotation(CcpJsonFieldsValidation.class);
		String actionName = "save(" +this.entityClass.getSimpleName();
		CcpJsonFieldsValidations.validate(annotation, json.content, actionName);
	}
	
}

package com.ccp.especifications.db.utils.decorators.engine;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.bulk.CcpBulkItem;
import com.ccp.especifications.db.bulk.CcpEntityBulkOperationType;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpEntityCrudOperationType;
import com.ccp.especifications.db.utils.CcpEntityField;
import com.ccp.validation.CcpJsonFieldsValidations;
import com.ccp.validation.annotations.CcpJsonFieldsValidation;

final class DefaultImplementationEntity implements CcpEntity{

	final Class<?> entityClass;
	final CcpEntityField[] fields;
	final String entityName;

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
		.map(x -> this.getMainBulkItem(x, CcpEntityBulkOperationType.create))
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

	
	public CcpEntity validateJson(CcpJsonRepresentation json) {
		boolean hasNotAnnotation = this.entityClass.isAnnotationPresent(CcpJsonFieldsValidation.class) == false;
		
		if(hasNotAnnotation) {
			return this;
		}
		
		CcpJsonFieldsValidation annotation = this.entityClass.getAnnotation(CcpJsonFieldsValidation.class);
		String actionName = "save(" +this.entityClass.getSimpleName();
		CcpJsonFieldsValidations.validate(annotation, json.content, actionName);
		return this;
	}

	public List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> getStepsBefore(
			CcpEntityCrudOperationType operation) {
		List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> stepsBefore = operation.getStepsBefore(this.entityClass);
		return stepsBefore;
	}

	public List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> getStepsAfter(
			CcpEntityCrudOperationType operation) {
		List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> stepsAfter = operation.getStepsAfter(this.entityClass);
		return stepsAfter;
	}

	public CcpEntity validateJson(CcpEntityCrudOperationType operation, CcpJsonRepresentation json) {
		operation.validate(this.entityClass, operation, json);
		return this;
	}

	
	
	
}

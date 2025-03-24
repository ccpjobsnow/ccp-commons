package com.ccp.especifications.db.utils.decorators.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.bulk.CcpBulkItem;
import com.ccp.especifications.db.bulk.CcpEntityBulkOperationType;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpEntityField;
import com.ccp.validation.CcpJsonFieldsValidations;
import com.ccp.validation.annotations.CcpJsonFieldsValidation;

final class DefaultImplementationEntity implements CcpEntity{

	final List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> jsonTransformers;
	final Class<?> entityClass;
	final CcpEntityField[] fields;
	final String entityName;

	public DefaultImplementationEntity(String entityName, Class<?> entityClass, List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> jsonTransformers, CcpEntityField... fields) {
		this.jsonTransformers = jsonTransformers;
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
		.map(x -> this.toBulkItem(x, CcpEntityBulkOperationType.create))
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

	public List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> getJsonTransformers() {
		ArrayList<Function<CcpJsonRepresentation, CcpJsonRepresentation>> arrayList = new ArrayList<>(this.jsonTransformers);
		return arrayList;
	}
	
}

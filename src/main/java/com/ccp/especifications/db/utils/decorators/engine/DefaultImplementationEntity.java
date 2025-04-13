package com.ccp.especifications.db.utils.decorators.engine;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.bulk.CcpBulkItem;
import com.ccp.especifications.db.bulk.CcpEntityBulkOperationType;
import com.ccp.especifications.db.bulk.handlers.CcpBulkHandlerTransferRecordToReverseEntity;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpEntityCrudOperationType;
import com.ccp.especifications.db.utils.CcpEntityField;

final class DefaultImplementationEntity implements CcpEntity{

	final CcpBulkHandlerTransferRecordToReverseEntity entityTransferRecordToReverseEntity;
	final CcpEntityField[] fields;
	final Class<?> entityClass;
	final String entityName;

	public DefaultImplementationEntity(String entityName, Class<?> entityClass, CcpBulkHandlerTransferRecordToReverseEntity entityTransferRecordToReverseEntity, CcpEntityField... fields) {
		this.entityTransferRecordToReverseEntity = entityTransferRecordToReverseEntity;
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

	public CcpJsonRepresentation getTransformedJsonBeforeOperation(CcpJsonRepresentation json, CcpEntityCrudOperationType operation) {
		// TODO Auto-generated method stub
		return null;
	}

	public CcpJsonRepresentation getTransformedJsonAfterOperation(CcpJsonRepresentation json, CcpEntityCrudOperationType operation) {
		// TODO Auto-generated method stub
		return null;
	}

	public CcpEntity validateJson(CcpJsonRepresentation json, CcpEntityCrudOperationType operation) {
		// TODO Auto-generated method stub
		return null;
	}

	public CcpBulkHandlerTransferRecordToReverseEntity getTransferRecordToReverseEntity() {
		return this.entityTransferRecordToReverseEntity;
	}

	
	
	
}

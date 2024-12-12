package com.ccp.especifications.db.utils.decorators;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.db.bulk.CcpBulkItem;
import com.ccp.especifications.db.bulk.CcpEntityOperationType;
import com.ccp.especifications.db.crud.CcpSelectUnionAll;
import com.ccp.especifications.db.utils.CcpDbRequester;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpEntityField;

final class BaseEntity implements CcpEntity{

	final String entityName;
	final CcpEntityField[] fields;
	

	public BaseEntity(String entityName, CcpEntityField... fields) {
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
			String entityName = ((BaseEntity)obj).getEntityName();
			String entityName2 = this.getEntityName();
			boolean equals = entityName.equals(entityName2);
			return equals;
		} catch (Exception e) {
			return false;
		}
	}

	public List<CcpJsonRepresentation> getParametersToSearch(CcpJsonRepresentation json) {
		
		String id = this.calculateId(json);

		CcpDbRequester dependency = CcpDependencyInjection.getDependency(CcpDbRequester.class);
		
		String fieldNameToEntity = dependency.getFieldNameToEntity();
		String fieldNameToId = dependency.getFieldNameToId();
		
		String entityName = this.getEntityName();
		
		CcpJsonRepresentation mainRecord = CcpConstants.EMPTY_JSON
		.put(fieldNameToEntity, entityName)
		.put(fieldNameToId, id)
		;
		List<CcpJsonRepresentation> asList = Arrays.asList(mainRecord);
		return asList;
	}

	public boolean isPresentInThisUnionAll(CcpSelectUnionAll unionAll, CcpJsonRepresentation json) {
		
		String index = this.getEntityName();
		String id = this.calculateId(json);

		boolean present = unionAll.isPresent(index, id);
		
		return present;
	}
	
	public CcpJsonRepresentation getRecordFromUnionAll(CcpSelectUnionAll unionAll, CcpJsonRepresentation json) {

		String id = this.calculateId(json);
		String index = this.getEntityName();
		
		CcpJsonRepresentation jsonValue = unionAll.getEntityRow(index, id);
		
		return jsonValue;
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

	public CcpBulkItem getRecordCopyToBulkOperation(CcpJsonRepresentation json, CcpEntityOperationType operation) {
		throw new UnsupportedOperationException();
	}
	

}

package com.ccp.especifications.db.bulk;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.crud.CcpSelectUnionAll;
import com.ccp.especifications.db.utils.CcpEntity;

public class CcpBulkItem {

	public final CcpEntityOperationType operation;
	public final CcpJsonRepresentation json;
	public final CcpEntity entity;
	public final String id;

	public CcpBulkItem (CcpJsonRepresentation json, CcpSelectUnionAll unionAll, CcpEntity entity) {

		boolean presentInThisUnionAll = entity.isPresentInThisUnionAll(unionAll, json);
		
		this.operation = presentInThisUnionAll ? CcpEntityOperationType.delete : CcpEntityOperationType.create;
		this.json = entity.getRequiredEntityRow(unionAll, json);
		this.id = entity.calculateId(json);
		this.entity = entity;
		
	}
	
	public CcpBulkItem(CcpJsonRepresentation json, CcpEntityOperationType operation, CcpEntity entity) {
		this.json = entity.getOnlyExistingFields(json);
		this.id = entity.calculateId(json);
		this.operation = operation;
		this.entity = entity;
	}

	public CcpBulkItem(CcpJsonRepresentation json, CcpEntityOperationType operation, CcpEntity entity, String id) {
		this.operation = operation;
		this.entity = entity;
		this.json = json;
		this.id = id;
	}


	public String toString() {
		CcpJsonRepresentation put = this.asMap();
		CcpJsonRepresentation jsonPiece = put.getJsonPiece("entity", "operation", "id");
		String string = jsonPiece.toString();
		return string;
	}

	public CcpJsonRepresentation asMap() {
		String entityName = this.entity.getEntityName();
		CcpJsonRepresentation put = CcpConstants.EMPTY_JSON
				.put("operation", this.operation)
				.put("entity", entityName)
				.put("json", this.json)
				.put("id", this.id);
		return put;
	}
	
	public int hashCode() {
		String string = this.entity + "_" + this.id + "_" + this.operation;
		int hashCode = string.hashCode();
		return hashCode;
	}

	public boolean equals(Object obj) {
		try {
			CcpBulkItem other = (CcpBulkItem)obj;
			
			boolean differentEntity = other.entity.equals(this.entity) == false;
			
			if(differentEntity) {
				return false;
			}
			
			boolean differentId = other.id.equals(this.id) == false;
			
			if(differentId) {
				return false;
			}
			boolean differentOperation = other.operation.equals(this.operation) == false;
			
			if(differentOperation) {
				return false;
			}
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public CcpBulkItem getSecondRecordToBulkOperation() {
		CcpBulkItem recordToBulkOperation = this.entity.getRecordCopyToBulkOperation(this.json, this.operation);
		return recordToBulkOperation;
	}
	
}

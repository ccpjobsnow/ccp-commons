package com.ccp.especifications.db.bulk;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.CcpEntity;

public class CcpBulkItem {

	public final CcpEntityOperationType operation;
	public final CcpJsonRepresentation json;
	public final CcpEntity entity;
	public final String id;

	public CcpBulkItem(CcpJsonRepresentation json, CcpEntityOperationType operation, CcpEntity entity) {
		this.json = entity.getOnlyExistingFields(json);
		this.id = entity.getId(json);
		this.operation = operation;
		this.entity = entity;
	}

	public CcpBulkItem(CcpJsonRepresentation json, CcpEntityOperationType operation, CcpEntity entity, String id) {
		this.operation = operation;
		this.entity = entity;
		this.json = json;
		this.id = id;
	}


	public CcpJsonRepresentation getJson() {
		return new CcpJsonRepresentation(this);
	}
	
	
	public String toString() {
		return "CcpBulkItem [operation=" + operation + ", json=" + json + ", entity=" + entity + ", id=" + id
				+ "]";
	}
	
	public int hashCode() {
		return (this.entity + "_" + this.id + "_" + this.operation).hashCode();
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
		CcpBulkItem recordToBulkOperation = this.entity.getRecordToBulkOperation(this.json, this.operation);
		return recordToBulkOperation;
	}
	
}

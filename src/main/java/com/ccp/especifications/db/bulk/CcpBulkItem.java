package com.ccp.especifications.db.bulk;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.CcpEntity;

public class CcpBulkItem {

	public final CcpEntityOperationType operation;
	public final CcpJsonRepresentation json;
	public final CcpEntity entity;
	public final String id;

	public CcpBulkItem(CcpJsonRepresentation json, CcpEntityOperationType operation, CcpEntity entity) {
		this.id = entity.getId(json);
		this.operation = operation;
		this.entity = entity;
		this.json = json;
	}


	public CcpJsonRepresentation getJson() {
		return new CcpJsonRepresentation(this);
	}
	
	
	public String toString() {
		return "CcpBulkItem [operation=" + operation + ", json=" + json + ", entity=" + entity + ", id=" + id
				+ "]";
	}
	
	public int hashCode() {
		return (this.entity + "_" + this.id).hashCode();
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
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}

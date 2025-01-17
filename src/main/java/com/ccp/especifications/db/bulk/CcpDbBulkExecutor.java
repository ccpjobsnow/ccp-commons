package com.ccp.especifications.db.bulk;

import java.util.List;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.CcpEntity;

public interface CcpDbBulkExecutor {
	
	CcpDbBulkExecutor clearRecords();
	
	default CcpDbBulkExecutor addRecord(CcpJsonRepresentation json, CcpEntityOperationType operation, CcpEntity entity) {
		
		CcpBulkItem bulkItem =  entity.toBulkItem(json, operation);
		CcpDbBulkExecutor addRecord = this.addRecord(bulkItem);
		return addRecord;
	}
	
	CcpDbBulkExecutor addRecord(CcpBulkItem bulkItem);

	default CcpDbBulkExecutor addRecords(List<CcpBulkItem> items) {
		CcpDbBulkExecutor bulk = this;
		for (CcpBulkItem item : items) {
			bulk = bulk.addRecord(item);
		}
		return bulk;
	}
	
	default CcpDbBulkExecutor addRecords(List<CcpJsonRepresentation> records, CcpEntityOperationType operation, CcpEntity entity) {
		CcpDbBulkExecutor bulk = this;
		for (CcpJsonRepresentation _record : records) {
			bulk = bulk.addRecord(_record, operation, entity);
		}
		return bulk;
	}
	
	List<CcpBulkOperationResult> getBulkOperationResult();
	
}

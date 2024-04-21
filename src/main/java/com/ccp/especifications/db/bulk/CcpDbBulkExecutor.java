package com.ccp.especifications.db.bulk;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.db.utils.CcpEntity;

public interface CcpDbBulkExecutor {
	
	default CcpDbBulkExecutor addRecord(CcpJsonRepresentation _record, CcpEntityOperationType operation, CcpEntity entity) {
		
		CcpBulkItem bulkItem = new CcpBulkItem(_record, operation, entity);
		CcpDbBulkExecutor addRecord = this.addRecord(bulkItem);
		return addRecord;
	}
	
	CcpDbBulkExecutor addRecord(CcpBulkItem bulkItem);
	
	List<CcpJsonRepresentation> getFailedRecords(List<CcpJsonRepresentation> auditRecords);

	List<CcpJsonRepresentation> getSuccedRecords(List<CcpJsonRepresentation> auditRecords);
 	
	CcpJsonRepresentation getAuditRecord(CcpBulkItem bulkItem, CcpJsonRepresentation bulkResult);

	default List<CcpJsonRepresentation> getAuditRecords(CcpJsonRepresentation bulkResult){
		
		List<CcpBulkItem> bulkItems = this.getBulkItems();
		
		List<CcpJsonRepresentation> collect = bulkItems.stream()
		.map(bulkItem -> this.getAuditRecord(bulkItem, bulkResult))
		.collect(Collectors.toList());

		return collect;
	}
	
	default CcpJsonRepresentation commitAndAuditLogingErrors(
			CcpEntity errorEntity, 
			CcpEntity auditEntity,
			Function<CcpJsonRepresentation, CcpJsonRepresentation> mapperSuccess,
			Function<CcpJsonRepresentation, CcpJsonRepresentation> mapperFail
			) {
		
		CcpJsonRepresentation bulkResultFromRecords = this.getBulkResult();
		List<CcpJsonRepresentation> auditRecords = this.getAuditRecords(bulkResultFromRecords);
		
		List<CcpJsonRepresentation> succedRecords = this.getSuccedRecords(auditRecords.stream().map(mapperSuccess).collect(Collectors.toList()));
		List<CcpJsonRepresentation> failedRecords = this.getFailedRecords(auditRecords).stream().map(mapperFail).collect(Collectors.toList());
		
		List<CcpBulkItem> collect2 = this.getRecordList(errorEntity, failedRecords);
		List<CcpBulkItem> collect = this.getRecordList(auditEntity, succedRecords);
		collect.addAll(collect2);
		CcpDbBulkExecutor bulk = CcpDependencyInjection.getDependency(CcpDbBulkExecutor.class);
		
		for (CcpBulkItem _record : collect) {
			bulk = bulk.addRecord(_record);
		}
		CcpJsonRepresentation bulkResultFromErrorsAndAuding = bulk.getBulkResult();
		
		CcpJsonRepresentation bulkResult = bulkResultFromRecords.putAll(bulkResultFromErrorsAndAuding);
		
		return bulkResult;
	}

	default List<CcpBulkItem> getRecordList(CcpEntity entity, List<CcpJsonRepresentation> records) {
		List<CcpBulkItem> collect = records.stream().map(x -> new CcpBulkItem(x, CcpEntityOperationType.create, entity)).collect(Collectors.toList());
		ArrayList<CcpBulkItem> arrayList = new ArrayList<CcpBulkItem>(collect);
		return arrayList;
	}
	
	List<CcpBulkItem> getBulkItems();
	
	CcpJsonRepresentation getBulkResult();
	
	default CcpDbBulkExecutor addRecords(List<CcpJsonRepresentation> records, CcpEntityOperationType operation, CcpEntity entity) {
		CcpDbBulkExecutor bulk = this;
		for (CcpJsonRepresentation _record : records) {
			bulk = bulk.addRecord(_record, operation, entity);
		}
		return bulk;
	}
}

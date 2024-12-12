package com.ccp.especifications.db.utils.decorators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.bulk.CcpBulkItem;
import com.ccp.especifications.db.bulk.CcpEntityOperationType;
import com.ccp.especifications.db.utils.CcpEntity;

public interface CcpEntityConfigurator {

	default List<CcpBulkItem> getFirstRecordsToInsert(){
		return new ArrayList<>();
	}
	
	default List<CcpBulkItem> toCreateBulkItems(CcpEntity entity, String... jsons){
		List<CcpBulkItem> collect = Arrays.asList(jsons).stream()
		.map(json -> new CcpJsonRepresentation(json))
		.map(json -> new CcpBulkItem(json, CcpEntityOperationType.create, entity))
		.collect(Collectors.toList());
		return collect;
	}
}

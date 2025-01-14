
package com.ccp.especifications.db.bulk;

import java.util.function.Function;

import com.ccp.constantes.CcpOtherConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.exceptions.db.CcpEntityRecordNotFound;

public enum CcpEntityOperationType {

	create(CcpOtherConstants.EMPTY_JSON.put("409", (Function<CcpBulkItem,CcpBulkItem>) x -> getUpdateOperationType(x))), 
	update(CcpOtherConstants.EMPTY_JSON.put("404", (Function<CcpBulkItem,CcpBulkItem>) x -> getCreateOperationType(x))), 
	delete(CcpOtherConstants.EMPTY_JSON.put("404", (Function<CcpBulkItem,CcpBulkItem>) x -> 
	{
		throw new CcpEntityRecordNotFound(x.entity, x.json);
	}))
	;
	
	private final CcpJsonRepresentation handlers;

	private CcpEntityOperationType(CcpJsonRepresentation handlers) {
		this.handlers = handlers;
	}
	private static CcpBulkItem getUpdateOperationType(CcpBulkItem x) {
		CcpBulkItem ccpBulkItem = new CcpBulkItem(x.json, CcpEntityOperationType.update, x.entity);
		return ccpBulkItem;
	}
	private static CcpBulkItem getCreateOperationType(CcpBulkItem x) {
		CcpBulkItem ccpBulkItem = new CcpBulkItem(x.json, CcpEntityOperationType.update, x.entity);
		return ccpBulkItem;
	}
	
	public CcpBulkItem getReprocess(Function<CcpBulkOperationResult, CcpJsonRepresentation> reprocessJsonProducer, CcpBulkOperationResult result, CcpEntity entityToReprocess) {
		
		int status = result.status();
		boolean statusNotFound = this.handlers.containsAllFields("" + status) == false;
		
		if(statusNotFound) {
			CcpJsonRepresentation json = reprocessJsonProducer.apply(result);
			CcpBulkItem ccpBulkItem = new CcpBulkItem(json, create, entityToReprocess);
			return ccpBulkItem;
		}
		
		Function<CcpBulkItem,CcpBulkItem> handler = this.handlers.getAsObject("" + status);
		CcpBulkItem bulkItem = result.getBulkItem();
		CcpBulkItem apply = handler.apply(bulkItem);
		return apply;
	}
}

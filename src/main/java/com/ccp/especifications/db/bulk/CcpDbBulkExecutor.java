package com.ccp.especifications.db.bulk;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpOperationType;

public interface CcpDbBulkExecutor {

	CcpMapDecorator commit(List<CcpMapDecorator> records, CcpOperationType bulkOperation, CcpEntity entity, CcpBulkAudit audit);
	
}

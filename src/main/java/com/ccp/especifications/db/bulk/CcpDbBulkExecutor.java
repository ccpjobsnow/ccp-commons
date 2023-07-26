package com.ccp.especifications.db.bulk;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpDbBulkExecutor {

	CcpMapDecorator commit(List<CcpMapDecorator> records, String bulkOperation, CcpBulkable bulkable);
	
}

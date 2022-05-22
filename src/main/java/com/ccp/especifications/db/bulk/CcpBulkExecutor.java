package com.ccp.especifications.db.bulk;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpBulkExecutor {

	CcpMapDecorator commit(List<CcpMapDecorator> records, CcpBulkOperation bulkOperation, String index);
	
}

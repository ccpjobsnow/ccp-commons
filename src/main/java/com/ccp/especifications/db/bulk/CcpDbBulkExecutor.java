package com.ccp.especifications.db.bulk;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.table.CcpDbTable;

public interface CcpDbBulkExecutor {

	CcpMapDecorator commit(List<CcpMapDecorator> records, CcpBulkOperation bulkOperation, CcpDbTable table);
	
}

package com.ccp.especifications.db.bulk;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpDbTable;

public interface CcpDbBulkExecutor {

	CcpMapDecorator commit(List<CcpMapDecorator> records, CcpBulkOperation bulkOperation, CcpDbTable table);
	
}

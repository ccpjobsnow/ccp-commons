package com.ccp.especifications.db.bulk;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpOperationType;

public interface CcpBulkAudit {
	void commit(List<CcpMapDecorator> records, CcpOperationType operation, CcpMapDecorator bulkResult);
}

package com.ccp.especifications.db.bulk;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpBulkAudit {
	CcpMapDecorator commit(List<CcpMapDecorator> records, CcpMapDecorator bulkResult);
}

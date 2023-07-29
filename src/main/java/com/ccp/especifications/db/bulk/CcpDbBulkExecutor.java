package com.ccp.especifications.db.bulk;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpOperationType;

public interface CcpDbBulkExecutor {

	default CcpMapDecorator commitAndAudit(List<CcpMapDecorator> records, CcpOperationType operation, CcpEntity entity, CcpEntity auditEntity, CcpEntity errorEntity) {
		CcpMapDecorator bulkResult = this.commit(records, operation, entity);
		this.audit(records, operation, bulkResult, entity, auditEntity, errorEntity);
		return bulkResult;
	}

	CcpMapDecorator commit(List<CcpMapDecorator> records, CcpOperationType operation, CcpEntity entity);

	void audit(List<CcpMapDecorator> records, CcpOperationType operation, CcpMapDecorator bulkResult, CcpEntity entity, CcpEntity auditEntity, CcpEntity errorEntity);
}

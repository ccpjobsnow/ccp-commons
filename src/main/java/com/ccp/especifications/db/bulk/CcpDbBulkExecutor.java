package com.ccp.especifications.db.bulk;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpEntityOperationType;

public interface CcpDbBulkExecutor {

	default CcpMapDecorator commitAndAuditAndSaveErrors(List<CcpMapDecorator> records, CcpEntityOperationType operation, CcpEntity entity, CcpEntity auditEntity, CcpEntity errorEntity) {
		
		CcpMapDecorator errorsAndSuccess = this.commit(records, operation, entity);
		
		this.audit(errorEntity, auditEntity, errorsAndSuccess, operation);

		this.saveErrors(auditEntity, errorEntity, errorsAndSuccess, operation);
		
		return errorsAndSuccess;
	}

	CcpMapDecorator commit(List<CcpMapDecorator> records, CcpEntityOperationType operation, CcpEntity entity);

	void audit(CcpEntity entity, CcpEntity auditEntity, CcpMapDecorator errorsAndSuccess,  CcpEntityOperationType operation);

	void saveErrors(CcpEntity entity, CcpEntity errorEntity, CcpMapDecorator errorsAndSuccess,  CcpEntityOperationType operation);

}

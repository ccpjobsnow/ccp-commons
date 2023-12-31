package com.ccp.especifications.db.bulk;

import java.util.List;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpEntityOperationType;

public interface CcpDbBulkExecutor {

	default CcpJsonRepresentation commitAndAuditAndSaveErrors(List<CcpJsonRepresentation> records, CcpEntityOperationType operation, CcpEntity entity, CcpEntity auditEntity, CcpEntity errorEntity) {
		
		CcpJsonRepresentation errorsAndSuccess = this.commit(records, operation, entity);
		
		this.audit(errorEntity, auditEntity, errorsAndSuccess, operation);

		this.saveErrors(auditEntity, errorEntity, errorsAndSuccess, operation);
		
		return errorsAndSuccess;
	}

	CcpJsonRepresentation commit(List<CcpJsonRepresentation> records, CcpEntityOperationType operation, CcpEntity entity);

	void audit(CcpEntity entity, CcpEntity auditEntity, CcpJsonRepresentation errorsAndSuccess,  CcpEntityOperationType operation);

	void saveErrors(CcpEntity entity, CcpEntity errorEntity, CcpJsonRepresentation errorsAndSuccess,  CcpEntityOperationType operation);

}

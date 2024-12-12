package com.ccp.especifications.db.utils.decorators;

import com.ccp.especifications.db.utils.CcpEntity;

public interface CcpEntityExpurgableFactory {

	CcpEntity getEntity(CcpEntity entity, CcpEntityExpurgableOptions timeOption);
	
}

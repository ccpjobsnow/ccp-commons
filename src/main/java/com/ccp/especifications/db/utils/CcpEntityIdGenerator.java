package com.ccp.especifications.db.utils;

import com.ccp.decorators.CcpJsonRepresentation;

public interface CcpEntityIdGenerator {

	CcpJsonRepresentation getOnlyExistingFields(CcpJsonRepresentation values);

	String getId(CcpJsonRepresentation values);

	String getEntityName();
	
}

package com.ccp.especifications.db.crud;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.CcpEntityIdGenerator;

public interface WhenRecordIsFoundInUnionAll<T> {

	T whenRecordIsFound(CcpJsonRepresentation searchParameter, CcpJsonRepresentation recordFound);

	T whenRecordIsNotFound(CcpJsonRepresentation searchParameter);
	
	CcpEntityIdGenerator getEntity();
	
}

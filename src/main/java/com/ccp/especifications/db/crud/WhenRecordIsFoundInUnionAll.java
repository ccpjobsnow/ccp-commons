package com.ccp.especifications.db.crud;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.CcpEntity;

public interface WhenRecordIsFoundInUnionAll<T> {

	T whenRecordExists(CcpJsonRepresentation searchParameter, CcpJsonRepresentation recordFound);

	T whenRecordDoesNotExist(CcpJsonRepresentation searchParameter);
	
	CcpEntity getEntity();
	
}

package com.ccp.especifications.db.utils;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpEntityIdGenerator {

	CcpMapDecorator getOnlyExistingFields(CcpMapDecorator values);

	String getId(CcpMapDecorator values);

	String name();
	
}

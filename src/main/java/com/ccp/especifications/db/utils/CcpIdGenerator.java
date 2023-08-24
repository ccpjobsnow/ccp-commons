package com.ccp.especifications.db.utils;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpIdGenerator {

	String getId(CcpMapDecorator values);

	String name();
	

}

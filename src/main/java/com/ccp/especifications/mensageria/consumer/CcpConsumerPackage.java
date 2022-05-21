package com.ccp.especifications.mensageria.consumer;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpConsumerPackage {
	CcpMapDecorator asMap();
	
	String asString();
}

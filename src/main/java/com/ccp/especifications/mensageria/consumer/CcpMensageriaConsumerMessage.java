package com.ccp.especifications.mensageria.consumer;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpMensageriaConsumerMessage {
	CcpMapDecorator asMap();
	String asString();
}

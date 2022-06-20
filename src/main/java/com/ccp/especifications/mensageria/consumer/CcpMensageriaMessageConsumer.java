package com.ccp.especifications.mensageria.consumer;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpMensageriaMessageConsumer {
	CcpMapDecorator onConsumeMessage(CcpMapDecorator message);
}

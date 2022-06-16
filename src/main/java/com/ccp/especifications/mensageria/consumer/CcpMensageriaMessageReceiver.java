package com.ccp.especifications.mensageria.consumer;

public interface CcpMensageriaMessageReceiver {
	void onConsumeMessage(CcpMensageriaConsumerMessage message, CcpMensageriaConsumerAck consumer);
}

package com.ccp.especifications.mensageria.consumer;

public interface CcpMessageConsumer {

	void onConsumeMessage(CcpMessagePackage pck, CcpConsumerPackage consumer, String topic);
}

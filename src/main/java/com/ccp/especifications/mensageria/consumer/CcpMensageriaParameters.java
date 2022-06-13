package com.ccp.especifications.mensageria.consumer;

import java.util.function.Function;

import com.ccp.decorators.CcpMapDecorator;

public class CcpMensageriaParameters {
	public final String topicName;
	public final String tenantName;
	public final Object credentialsFile;
	public final int threadsQuantity;
	public final CcpMapDecorator otherParameters;
	public final Function<CcpMensageriaParameters, Object> messageReceiverProducer;
	public CcpMensageriaParameters(String topicName, String tenantName, Object credentialsFile, int threadsQuantity,
			Function<CcpMensageriaParameters, Object> messageReceiverProducer, CcpMapDecorator otherParameters) {
		this.messageReceiverProducer = messageReceiverProducer;
		this.credentialsFile = credentialsFile;
		this.threadsQuantity = threadsQuantity;
		this.otherParameters = otherParameters;
		this.tenantName = tenantName;
		this.topicName = topicName;
	}

	@SuppressWarnings("unchecked")
	public <T> T getMessageReceiver() {
		return(T) this.messageReceiverProducer.apply(this);
	}

	@SuppressWarnings("unchecked")
	public <U> U getCredentialsFile() {
		return (U) this.credentialsFile;
	}
	
}

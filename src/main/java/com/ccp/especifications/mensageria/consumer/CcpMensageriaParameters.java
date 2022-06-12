package com.ccp.especifications.mensageria.consumer;

import com.ccp.decorators.CcpMapDecorator;

public class CcpMensageriaParameters {
	public final String topicName;
	public final String tenantName;
	public final Object credentialsFile;
	public final int threadsQuantity;
	public final Object messageReceiver;
	public final CcpMapDecorator otherParameters;
	public CcpMensageriaParameters(String topicName, String tenantName, Object credentialsFile, int threadsQuantity,
			Object messageReceiver, CcpMapDecorator otherParameters) {
		this.credentialsFile = credentialsFile;
		this.threadsQuantity = threadsQuantity;
		this.messageReceiver = messageReceiver;
		this.otherParameters = otherParameters;
		this.tenantName = tenantName;
		this.topicName = topicName;
	}

	@SuppressWarnings("unchecked")
	public <T> T getMessageReceiver() {
		return (T) this.messageReceiver;
	}

	@SuppressWarnings("unchecked")
	public <T> T getCredentialsFile() {
		return (T) this.credentialsFile;
	}
	
}

package com.ccp.especifications.mensageria.consumer;

import java.io.InputStream;

import com.ccp.decorators.CcpMapDecorator;

public class CcpMensageriaParameters {
	public final String topicName;
	public final String tenantName;
	public final int threadsQuantity;
	public final InputStream credentials;
	public final CcpMapDecorator otherParameters;
	public final CcpMensageriaMessageReceiver messageReceiver;
	public CcpMensageriaParameters(String topicName, String tenantName, int threadsQuantity,
			InputStream credentials, CcpMapDecorator otherParameters,
			CcpMensageriaMessageReceiver messageReceiver) {
		this.topicName = topicName;
		this.tenantName = tenantName;
		this.threadsQuantity = threadsQuantity;
		this.credentials = credentials;
		this.otherParameters = otherParameters;
		this.messageReceiver = messageReceiver;
	}
	
	
	


	
}

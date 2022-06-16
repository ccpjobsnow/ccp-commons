package com.ccp.especifications.mensageria.consumer;

import java.io.InputStream;

import com.ccp.decorators.CcpMapDecorator;

public class CcpMensageriaParameters {
	
	public final CcpMensageriaMessageReceiver messageReceiver;
	public final CcpMapDecorator otherParameters;
	public final InputStream credentials;
	public final int threadsQuantity;
	public final String tenantName;
	public final String topicName;
	
	public CcpMensageriaParameters(String topicName, String tenantName, 
			int threadsQuantity, InputStream credentials, 
			CcpMapDecorator otherParameters,
			CcpMensageriaMessageReceiver messageReceiver) {

		this.threadsQuantity = threadsQuantity;
		this.otherParameters = otherParameters;
		this.messageReceiver = messageReceiver;
		this.credentials = credentials;
		this.tenantName = tenantName;
		this.topicName = topicName;
	}
	
	
	


	
}

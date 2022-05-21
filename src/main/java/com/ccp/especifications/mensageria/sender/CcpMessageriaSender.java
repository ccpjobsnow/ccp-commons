package com.ccp.especifications.mensageria.sender;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpMessageriaSender {

	void send(CcpMapDecorator msg, String topic);

	void send(String msg, String topic);
	
	
}

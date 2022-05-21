package com.ccp.especifications.mensageria.sender;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpMessageriaSender {

	void send(CcpMapDecorator msg);

	void send(String msg);
	
	
}

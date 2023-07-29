package com.ccp.especifications.mensageria.sender;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpMensageriaSender {

	default void send(CcpMapDecorator msg, CcpMensageriaTopic topic) {
		this.send(msg.asJson(), topic);
	}

	void send(String msg, CcpMensageriaTopic topic);
	
	
}

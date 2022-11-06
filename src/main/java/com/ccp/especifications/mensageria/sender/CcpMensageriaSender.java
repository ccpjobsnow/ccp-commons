package com.ccp.especifications.mensageria.sender;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpMensageriaSender {

	default CcpMapDecorator send(CcpMapDecorator msg, CcpMensageriaTopic topic) {
		this.send(msg.asJson(), topic);
		return msg;
	}

	void send(String msg, CcpMensageriaTopic topic);
	
	
}

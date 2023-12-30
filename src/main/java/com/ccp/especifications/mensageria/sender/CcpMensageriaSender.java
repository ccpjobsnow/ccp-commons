package com.ccp.especifications.mensageria.sender;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpMensageriaSender {

	default void send(Enum<?> topic, CcpMapDecorator msg) {
		this.send(topic, msg.asUgglyJson());
	}

	void send(Enum<?> topic, String... msgs);
	
	
}

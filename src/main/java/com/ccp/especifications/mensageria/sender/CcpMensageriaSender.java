package com.ccp.especifications.mensageria.sender;

import com.ccp.decorators.CcpJsonRepresentation;

public interface CcpMensageriaSender {

	default void send(Enum<?> topic, CcpJsonRepresentation msg) {
		this.send(topic, msg.asUgglyJson());
	}

	void send(Enum<?> topic, String... msgs);
	
	
}

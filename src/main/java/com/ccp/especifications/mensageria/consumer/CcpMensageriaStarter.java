package com.ccp.especifications.mensageria.consumer;

import java.util.function.Function;

public interface CcpMensageriaStarter {
	void synchronize(String topicName, Function<String, CcpMensageriaParameters> producer);
}

package com.ccp.especifications.mensageria.sender;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpJsonRepresentation;

public interface CcpMensageriaSender {

	default void send(CcpTopic topic, CcpJsonRepresentation... msgs) {
		String[] array = Arrays.asList(msgs).stream().map(x -> x.asUgglyJson()).collect(Collectors.toList())
		.toArray(new String[msgs.length]);
		this.send(topic, array);
	}
	
	void send(CcpTopic topic, String... msgs);
	
	
}

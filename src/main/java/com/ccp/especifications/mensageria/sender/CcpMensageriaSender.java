package com.ccp.especifications.mensageria.sender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpJsonRepresentation;

public interface CcpMensageriaSender {

	default void send (String topic, List<CcpJsonRepresentation> msgs) {
		int size = msgs.size();
		CcpJsonRepresentation[] a = new CcpJsonRepresentation[size];
		CcpJsonRepresentation[] array = msgs.toArray(a);
		this.send(topic, array);
	}
	
	default void send(String topic, CcpJsonRepresentation... msgs) {
		String[] array = Arrays.asList(msgs).stream().map(x -> x.asUgglyJson()).collect(Collectors.toList())
		.toArray(new String[msgs.length]);
		this.send(topic, array);
	}
	
	void send(String topic, String... msgs);

	
	
}

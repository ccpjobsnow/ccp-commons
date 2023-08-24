package com.ccp.constantes;

import java.util.function.Consumer;
import java.util.function.Function;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpConstants {

	String CHARACTERS_TO_GENERATE_TOKEN = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	Function<CcpMapDecorator, CcpMapDecorator> RETURNS_EMPTY_JSON = x -> CcpConstants.EMPTY_JSON;
	CcpMapDecorator EMPTY_JSON = new CcpMapDecorator();
	Consumer<String> EXECUTE_NOTHING = x -> {};
	Function<CcpMapDecorator, CcpMapDecorator> DO_NOTHING = x -> x;

}

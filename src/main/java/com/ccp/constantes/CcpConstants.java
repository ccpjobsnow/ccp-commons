package com.ccp.constantes;

import java.util.function.Consumer;
import java.util.function.Predicate;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpProcess;
import com.google.gson.Gson;

public interface CcpConstants {
	CcpMapDecorator EMPTY_JSON = new CcpMapDecorator();
	CcpProcess RETURNS_EMPTY_JSON = x -> CcpConstants.EMPTY_JSON;
	CcpProcess DO_NOTHING = x -> x;
	Gson GSON = new Gson();
	Predicate<String> TO_DISCARD = x -> false;
	Consumer<String> EXECUTE_NOTHING = x -> {};


}

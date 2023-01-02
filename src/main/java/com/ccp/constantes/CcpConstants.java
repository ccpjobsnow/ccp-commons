package com.ccp.constantes;

import java.util.function.Consumer;
import java.util.function.Predicate;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpProcess;
import com.google.gson.Gson;

public interface CcpConstants {

	String CHARACTERS_TO_GENERATE_TOKEN = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%*&_-+=()";
	CcpProcess RETURNS_EMPTY_JSON = x -> CcpConstants.EMPTY_JSON;
	CcpMapDecorator EMPTY_JSON = new CcpMapDecorator();
	Consumer<String> EXECUTE_NOTHING = x -> {};
	Predicate<String> TO_DISCARD = x -> false;
	CcpProcess DO_NOTHING = x -> x;
	Gson GSON = new Gson();

}

package com.ccp.constantes;

import java.util.function.Consumer;
import java.util.function.Predicate;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpProcess;
import com.google.gson.Gson;

public interface CcpConstants {
	CcpMapDecorator emptyJson = new CcpMapDecorator();
	CcpProcess returnEmpty = x -> CcpConstants.emptyJson;
	CcpProcess doNothing = x -> x;
	Gson gson = new Gson();
	Predicate<String> doNotEvaluate = x -> false;
	Consumer<String> byPass = x -> {};
	CcpProcess getFirstTry = values -> values.getSubMap("email").put("tries",0);


}

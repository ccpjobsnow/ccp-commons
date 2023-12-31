package com.ccp.constantes;

import java.util.function.Consumer;
import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;

public interface CcpConstants {

	String CHARACTERS_TO_GENERATE_TOKEN = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	Function<CcpJsonRepresentation, CcpJsonRepresentation> RETURNS_EMPTY_JSON = x -> CcpConstants.EMPTY_JSON;
	CcpJsonRepresentation EMPTY_JSON = CcpJsonRepresentation.getEmptyJson();
	Consumer<String> EXECUTE_NOTHING = x -> {};
	Function<CcpJsonRepresentation, CcpJsonRepresentation> DO_NOTHING = x -> x;
}

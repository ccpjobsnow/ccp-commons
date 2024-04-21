package com.ccp.constantes;

import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;

public interface CcpConstants {

	Function<CcpJsonRepresentation, CcpJsonRepresentation> RETURNS_EMPTY_JSON = x -> CcpConstants.EMPTY_JSON;
	String CHARACTERS_TO_GENERATE_TOKEN = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	Function<CcpJsonRepresentation, CcpJsonRepresentation> DO_BY_PASS = x -> x;
	CcpJsonRepresentation EMPTY_JSON = CcpJsonRepresentation.getEmptyJson();
}

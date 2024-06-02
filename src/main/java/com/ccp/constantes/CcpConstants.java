package com.ccp.constantes;

import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;

public interface CcpConstants {

	String STRONG_PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
	Function<CcpJsonRepresentation, CcpJsonRepresentation> RETURNS_EMPTY_JSON = x -> CcpConstants.EMPTY_JSON;
	Function<CcpJsonRepresentation, CcpJsonRepresentation> DO_NOTHING = json -> json;
	CcpJsonRepresentation EMPTY_JSON = CcpJsonRepresentation.getEmptyJson();
	String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$"; 

}

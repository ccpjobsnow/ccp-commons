package com.ccp.especifications.async.business.factory;

import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;

public interface CcpAsyncBusinessFactory {

	Function<CcpJsonRepresentation, CcpJsonRepresentation> getAsyncBusiness(String processName);
	
}

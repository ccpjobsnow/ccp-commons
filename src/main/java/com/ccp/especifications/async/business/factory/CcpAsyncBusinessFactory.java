package com.ccp.especifications.async.business.factory;

import java.util.Map;
import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.exceptions.process.CcpProcessMissing;

public interface CcpAsyncBusinessFactory {

	default Function<CcpJsonRepresentation, CcpJsonRepresentation> getAsyncBusiness(String processName) {
		
		Map<String, Function<CcpJsonRepresentation, CcpJsonRepresentation>> map = this.getMap();
		Function<CcpJsonRepresentation, CcpJsonRepresentation> function = map.get(processName);
		
		if(function == null) {
			throw new CcpProcessMissing(processName);
		}
		
		return function;
	}
	
	Map<String, Function<CcpJsonRepresentation, CcpJsonRepresentation>> getMap();
	
	
}

package com.ccp.exceptions.process;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.async.business.factory.CcpAsyncBusinessFactory;

public class CcpAsyncTask {
	
	static Map<String, Function<CcpJsonRepresentation, CcpJsonRepresentation>> instances = new HashMap<String, Function<CcpJsonRepresentation,CcpJsonRepresentation>>();

	public static synchronized Function<CcpJsonRepresentation, CcpJsonRepresentation> getProcess(String processName) {
		
		boolean alreadyInstanced = instances.containsKey(processName);
		
		if(alreadyInstanced) {
			Function<CcpJsonRepresentation, CcpJsonRepresentation> instance = instances.get(processName);
			return instance;
		}
		
		CcpAsyncBusinessFactory asyncBusinessFactory = CcpDependencyInjection.getDependency(CcpAsyncBusinessFactory.class);
		Function<CcpJsonRepresentation, CcpJsonRepresentation> asyncBusiness = asyncBusinessFactory.getAsyncBusiness(processName);
		instances.put(processName, asyncBusiness);
		return asyncBusiness;
	}
	
}

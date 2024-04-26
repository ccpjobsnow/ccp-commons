package com.ccp.exceptions.process;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.async.business.factory.CcpAsyncBusinessFactory;
import com.ccp.especifications.db.utils.CcpEntity;

public class CcpAsyncTask {
	
	static Map<String, Function<CcpJsonRepresentation, CcpJsonRepresentation>> instances = new HashMap<String, Function<CcpJsonRepresentation,CcpJsonRepresentation>>();

	private static synchronized Function<CcpJsonRepresentation, CcpJsonRepresentation> getProcess(String processName) {
		
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
	
	public static CcpJsonRepresentation executeProcess(String processName, CcpJsonRepresentation values, CcpEntity entity,  Function<CcpJsonRepresentation, CcpJsonRepresentation> jnAsyncBusinessNotifyError) {
		
		String asyncTaskId = values.getAsString("asyncTaskId");
		
		CcpJsonRepresentation asyncTaskDetails = entity.getOneById(asyncTaskId);	
		
		try {
			CcpJsonRepresentation response = executeProcess(processName, values, jnAsyncBusinessNotifyError);
			saveProcessResult(entity, asyncTaskDetails, response,asyncTaskId, true);
			return response;
		} catch (Throwable e) {
			CcpJsonRepresentation response = new CcpJsonRepresentation(e);
			saveProcessResult(entity, asyncTaskDetails, response, asyncTaskId, false);
			throw e;
		}
	}

	public static CcpJsonRepresentation executeProcess(String processName, CcpJsonRepresentation values, Function<CcpJsonRepresentation, CcpJsonRepresentation> jnAsyncBusinessNotifyError) {
		Function<CcpJsonRepresentation, CcpJsonRepresentation> process = getProcess(processName);
		try {
			CcpJsonRepresentation response = process.apply(values);
			return response;
			
		} catch (Throwable e) {
			CcpJsonRepresentation errorDetails = new CcpJsonRepresentation(e);
			CcpJsonRepresentation renameKey = errorDetails.renameKey("message", "msg");
			jnAsyncBusinessNotifyError.apply(renameKey);
			throw e;
		}
	}

	private static void saveProcessResult(CcpEntity entity, CcpJsonRepresentation messageDetails, CcpJsonRepresentation response,String asyncTaskId, boolean success) {
		Long finished = System.currentTimeMillis();
		CcpJsonRepresentation processResult = messageDetails.put("response", response).put("finished", finished).put("success", success);
		entity.createOrUpdate(processResult, asyncTaskId);
	}

}
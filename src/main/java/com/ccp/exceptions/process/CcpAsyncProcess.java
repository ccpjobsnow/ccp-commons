package com.ccp.exceptions.process;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.decorators.CcpTimeDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.mensageria.sender.CcpMensageriaSender;
import com.ccp.especifications.mensageria.sender.CcpTopic;

public class CcpAsyncProcess {
	
	static Map<String, Function<CcpJsonRepresentation, CcpJsonRepresentation>> instances = new HashMap<String, Function<CcpJsonRepresentation,CcpJsonRepresentation>>();

	@SuppressWarnings("unchecked")
	private static synchronized Function<CcpJsonRepresentation, CcpJsonRepresentation> getProcess(String processName) {
		
		boolean alreadyInstanced = instances.containsKey(processName);
		
		if(alreadyInstanced) {
			Function<CcpJsonRepresentation, CcpJsonRepresentation> instance = instances.get(processName);
			return instance;
		}
		
		try {
			if(Class.forName(processName).getDeclaredConstructor().newInstance() instanceof Function instance) {
				instances.put(processName, instance);
				return instance;
			}
			throw new ClassCastException("the process '" + processName + "' is not a " + Function.class.getName() + "'s implementation");
		} catch (Exception e) {
			throw new RuntimeException("Problem with the process " + processName, e);
		}
	}
	
	public static CcpJsonRepresentation executeProcess(String processName, CcpJsonRepresentation values, CcpEntity entity) {
		
		String asyncTaskId = values.getAsString("asyncTaskId");
		
		CcpJsonRepresentation asyncTaskDetails = entity.getOneById(asyncTaskId);	
		
		try {
			CcpJsonRepresentation response = executeProcess(processName, values);
			saveProcessResult(entity, asyncTaskDetails, response,asyncTaskId, true);
			return response;
		} catch (Throwable e) {
			CcpJsonRepresentation response = new CcpJsonRepresentation(e);
			saveProcessResult(entity, asyncTaskDetails, response, asyncTaskId, false);
			throw e;
		}
	}

	public static CcpJsonRepresentation executeProcess(String processName, CcpJsonRepresentation values) {
		Function<CcpJsonRepresentation, CcpJsonRepresentation> process = getProcess(processName);
		CcpJsonRepresentation response = process.apply(values);
		return response;
	}

	private static void saveProcessResult(CcpEntity entity, CcpJsonRepresentation messageDetails, CcpJsonRepresentation response,String asyncTaskId, boolean success) {
		Long finished = System.currentTimeMillis();
		CcpJsonRepresentation processResult = messageDetails.put("response", response).put("finished", finished).put("success", success);
		entity.createOrUpdate(processResult, asyncTaskId);
	}
	final CcpMensageriaSender mensageriaSender = CcpDependencyInjection.hasDependency(CcpMensageriaSender.class) ? CcpDependencyInjection.getDependency(CcpMensageriaSender.class) : null;
	//TODO ENVIAR LISTA DE MENSAGENS DE UMA VEZ SÓ
	public CcpJsonRepresentation send(CcpJsonRepresentation values, CcpTopic topic, CcpEntity entity) {
		String token = new CcpStringDecorator(CcpConstants.CHARACTERS_TO_GENERATE_TOKEN).text().generateToken(20);
		CcpJsonRepresentation messageDetails = CcpConstants.EMPTY_JSON
				.put("id", token)
				.put("request", values)
				.put("topic", topic)
				.put("started", System.currentTimeMillis())
				.put("data", new CcpTimeDecorator().getFormattedCurrentDateTime("dd/MM/yyyy HH:mm:ss"))
				;
		
		String asyncTaskId = entity.getId(messageDetails);
		entity.createOrUpdate(messageDetails, asyncTaskId);
		
		this.mensageriaSender.send(topic, messageDetails);
		return messageDetails.put("asyncTaskId", asyncTaskId);
	}

}

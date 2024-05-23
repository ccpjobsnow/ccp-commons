package com.ccp.exceptions.db;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.db.crud.CcpSelectUnionAll;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.json.CcpJsonHandler;

@SuppressWarnings("serial")
public class CcpEntityRecordNotFound extends RuntimeException{

	public CcpEntityRecordNotFound(String entityName, String id) {
		super(getErrorMessage(entityName, id));
	}

	public CcpEntityRecordNotFound(CcpEntity entity, CcpJsonRepresentation values, CcpSelectUnionAll unionAll) {
		super(getErrorMessage(entity, values)+", " + CcpDependencyInjection.getDependency(CcpJsonHandler.class).toJson(unionAll));
	}
	

	private static String getErrorMessage(String entityName, String id) {
		String errorMessage = String.format("Does not exist an id '%s' registered in the entity '%s'.", 
				id,	entityName);

		return errorMessage;
	}


	private static String getErrorMessage(CcpEntity entity, CcpJsonRepresentation values) {

		CcpJsonRepresentation primaryKeyValues = entity.getPrimaryKeyValues(values);
		String entityName = entity.getEntityName();
		String id = entity.getId(values);
		
		String errorMessage = String.format("Does not exist an id '%s' registered in the entity '%s'. Values to compose this id are: %s ", 
				id,
				entityName,
				primaryKeyValues);

		return errorMessage;
	}
	
}

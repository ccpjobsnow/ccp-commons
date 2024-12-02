package com.ccp.especifications.db.crud;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.validation.CcpJsonFieldsValidations;
import com.ccp.validation.annotations.CcpJsonValidation;

public interface CcpCrud {

	default CcpJsonRepresentation createOrUpdate(CcpEntity entity, CcpJsonRepresentation json) {
		
		Class<? extends CcpEntity> class1 = entity.getClass();
		if(class1.isAnnotationPresent(CcpJsonValidation.class)) {
			CcpJsonValidation annotation = class1.getAnnotation(CcpJsonValidation.class);
			String actionName = "save" + entity.getClass().getSimpleName();
			CcpJsonFieldsValidations.validate(annotation, json.content, actionName);
		}
		
		String id = entity.calculateId(json);

		CcpJsonRepresentation response = this.createOrUpdate(entity, json, id);
		
		return response;
	}
	
	
	default boolean exists(CcpEntity entity, CcpJsonRepresentation json) {
		
		String id = entity.calculateId(json);
		
		boolean exists = this.exists(entity, id);
		
		return exists;
	}
	
	CcpJsonRepresentation getOneById(CcpEntity entity, String id);

	default CcpJsonRepresentation getOneById(CcpEntity entity, CcpJsonRepresentation json) {
	
		String id = entity.calculateId(json);
		
		CcpJsonRepresentation oneById = this.getOneById(entity, id);
		
		return oneById;
	}
	
	CcpSelectUnionAll unionAll(Collection<CcpJsonRepresentation> values, CcpEntity... entities);

	default CcpSelectUnionAll unionAll(CcpJsonRepresentation json, CcpEntity... entities) {
		List<CcpJsonRepresentation> asList = Arrays.asList(json);
		CcpSelectUnionAll unionAll = this.unionAll(asList, entities);
		return unionAll;
	}


	CcpJsonRepresentation createOrUpdate(CcpEntity entity, CcpJsonRepresentation json, String id);

	boolean exists(CcpEntity entity, String id);

	boolean delete(CcpEntity entity, String id); 
	
	default boolean delete(CcpEntity entity, CcpJsonRepresentation json) {
		String id = entity.calculateId(json);

		boolean deleted = this.delete(entity, id);
		return deleted;
	}
}

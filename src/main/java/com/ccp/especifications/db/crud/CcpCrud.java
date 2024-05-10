package com.ccp.especifications.db.crud;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.validation.CcpJsonFieldsValidations;
import com.ccp.validation.annotations.ValidationRules;

public interface CcpCrud {

	default CcpJsonRepresentation createOrUpdate(CcpEntity entity, CcpJsonRepresentation data) {
		
		Class<? extends CcpEntity> class1 = entity.getClass();
		if(class1.isAnnotationPresent(ValidationRules.class)) {
			ValidationRules annotation = class1.getAnnotation(ValidationRules.class);
			String actionName = "save" + entity.getClass().getSimpleName();
			CcpJsonFieldsValidations.validate(annotation, data.content, actionName);
		}
		
		String id = entity.getId(data);

		CcpJsonRepresentation response = this.createOrUpdate(entity, data, id);
		
		return response;
	}
	
	
	default boolean exists(CcpEntity entity, CcpJsonRepresentation values) {
		String id = entity.getId(values);
		
		boolean exists = this.exists(entity, id);
		return exists;
	}
	
	CcpJsonRepresentation getOneById(CcpEntity entity, String id);

	default CcpJsonRepresentation getOneById(CcpEntity entity, CcpJsonRepresentation values) {
	
		String id = entity.getId(values);
		
		CcpJsonRepresentation oneById = this.getOneById(entity, id);
		
		return oneById;
	}
	
	CcpSelectUnionAll unionAll(Collection<CcpJsonRepresentation> values, CcpEntity... entities);
	CcpSelectUnionAll unionAll(List<String> ids, CcpEntity... entities);

	default CcpSelectUnionAll unionAll(CcpJsonRepresentation values, CcpEntity... entities) {
		List<CcpJsonRepresentation> asList = Arrays.asList(values);
		CcpSelectUnionAll unionAll = this.unionAll(asList, entities);
		return unionAll;
	}


	CcpJsonRepresentation createOrUpdate(CcpEntity entity, CcpJsonRepresentation data, String id);

	boolean exists(CcpEntity entity, String id);

	boolean delete(CcpEntity entity, String id); 
	
	default boolean delete(CcpEntity entity, CcpJsonRepresentation values) {
		String id = entity.getId(values);

		boolean deleted = this.delete(entity, id);
		return deleted;
	}
}

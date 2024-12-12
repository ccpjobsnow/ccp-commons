package com.ccp.especifications.db.crud;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.cache.CcpCacheDecorator;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.validation.CcpJsonFieldsValidations;
import com.ccp.validation.annotations.CcpJsonFieldsValidation;

public interface CcpCrud {

	default CcpJsonRepresentation createOrUpdate(CcpEntity entity, CcpJsonRepresentation json) {
		
		Class<? extends CcpEntity> class1 = entity.getClass();
		if(class1.isAnnotationPresent(CcpJsonFieldsValidation.class)) {
			CcpJsonFieldsValidation annotation = class1.getAnnotation(CcpJsonFieldsValidation.class);
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
	
	
	CcpUnionAllExecutor getUnionAllExecutor();
	
	
	default CcpSelectUnionAll unionBetweenMainAndTwinEntities(CcpJsonRepresentation json, CcpEntity entity) {
		CcpEntity[] thisEntityAndHisTwinEntity = entity.getThisEntityAndHisTwinEntity();
		CcpSelectUnionAll unionAll = this.unionAll(json, thisEntityAndHisTwinEntity);
		return unionAll;
	}
	
	default CcpSelectUnionAll unionAll(CcpJsonRepresentation[] jsons, CcpEntity... entities) {
		this.deleteKeysInCache(jsons,  entities);
		List<CcpJsonRepresentation> asList = Arrays.asList(jsons);
		CcpUnionAllExecutor unionAllExecutor = this.getUnionAllExecutor();
		CcpSelectUnionAll unionAll = unionAllExecutor.unionAll(asList, entities);
		return unionAll;
	}


	
	default CcpSelectUnionAll unionAll(CcpJsonRepresentation json, CcpEntity... entities) {
		CcpJsonRepresentation[] jsons = new CcpJsonRepresentation[] {json};
		
		CcpSelectUnionAll unionAll = this.unionAll(jsons, entities);
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
	
	default void deleteKeysInCache(CcpJsonRepresentation[] jsons, CcpEntity... entities) {
		Set<String> keysToDeleteInCache = new HashSet<>();
		for (CcpEntity entity : entities) {
			for (CcpJsonRepresentation json : jsons) {
				CcpCacheDecorator cache = new CcpCacheDecorator(entity, json);
				keysToDeleteInCache.add(cache.key);
			}
		}
		
//		String[] array = keysToDeleteInCache.toArray(new String[keysToDeleteInCache.size()]);
		// FIXME this.deleteKeysInTheCache(array);
	}
	

}

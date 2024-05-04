package com.ccp.especifications.db.crud;

import java.util.List;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.CcpEntityIdGenerator;
import com.ccp.exceptions.db.CcpEntityRecordNotFound;

public class CcpDaoUnionAll {

	public final CcpJsonRepresentation  condensed;

	public CcpDaoUnionAll(List<CcpJsonRepresentation> results) {
		
		CcpJsonRepresentation  condensed = CcpConstants.EMPTY_JSON;
		
		for (CcpJsonRepresentation result : results) {
			String index = result.getAsString("_index");
			String id = result.getAsString("_id");
			CcpJsonRepresentation removeKeys = result.removeKeys("_index", "_id");
			condensed = condensed.putSubKey(index, id, removeKeys);
		}
		
		this.condensed = condensed;
	}
	
	public boolean isPresent(CcpEntityIdGenerator entity, CcpJsonRepresentation value) {
		
		String index = entity.getEntityName();
		String id = entity.getId(value);
		
		boolean present = this.isPresent(index, id);
		
		return present;
	}

	public boolean isPresent(String entityName, String id) {
		
		boolean entityNotFound = this.condensed.containsAllKeys(entityName) == false;
		
		if(entityNotFound) {
			return false;
		}
		//FIXME Verificar a estrutura do condensed
		CcpJsonRepresentation innerJson = this.condensed.getInnerJson(id);
		
		boolean idNotFound = innerJson.containsAllKeys(id) == false;
		
		if(idNotFound) {
			return false;
		}
		return true;
	}

	public boolean isPresent(CcpEntityIdGenerator entity) {
		
		String index = entity.getEntityName();
		
		boolean indexNotFound = this.condensed.containsAllKeys(index) == false;
		
		if(indexNotFound) {
			return false;
		}
		
		return true;
	}
	
	
	public <T> T whenRecordIsFoundInUnionAll(
			CcpJsonRepresentation searchParameter, 
			WhenRecordIsFoundInUnionAll<T> handler
			) {
		
		CcpEntityIdGenerator entity = handler.getEntity();
	
		boolean notFound = this.isPresent(entity, searchParameter) == false;
		
		if(notFound) {
			T whenRecordIsNotFound = handler.whenRecordIsNotFound(searchParameter);
			return whenRecordIsNotFound;
		}
		
		CcpJsonRepresentation recordFound = this.get(entity, searchParameter);
		
		T whenRecordIsFound = handler.whenRecordIsFound(searchParameter, recordFound);
		
		return whenRecordIsFound;
	}
	
	
	public CcpJsonRepresentation get(CcpEntityIdGenerator entity, CcpJsonRepresentation value) {
		
		String index = entity.getEntityName();
		String id = entity.getId(value);
		
		CcpJsonRepresentation jsonValue = this.get(index, id);
		return jsonValue;
	}

	public CcpJsonRepresentation get(String index, String id) {
		boolean indexNotFound = this.condensed.containsAllKeys(index) == false;
		
		if(indexNotFound) {
			throw new CcpEntityRecordNotFound(index, "");
		}
		
		
		CcpJsonRepresentation innerJson = this.condensed.getInnerJson(index);

		
		boolean idNotFound = innerJson.containsAllKeys(id) == false;
		
		if(idNotFound) {
			return CcpConstants.EMPTY_JSON;
		}
		
		CcpJsonRepresentation jsonValue = innerJson.getInnerJson(id);
		return jsonValue;
	}
	public CcpJsonRepresentation get(CcpEntityIdGenerator entity) {
		
		String index = entity.getEntityName();
		
		boolean indexNotFound = this.condensed.containsAllKeys(index) == false;
		
		if(indexNotFound) {
			throw new CcpEntityRecordNotFound(index, "");
		}
		
		
		CcpJsonRepresentation innerJson = this.condensed.getInnerJson(index);
		
		return innerJson;
	}

	@Override
	public String toString() {
		return this.condensed.toString();
	}
	
	
}

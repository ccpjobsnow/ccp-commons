package com.ccp.especifications.db.crud;

import java.util.List;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.db.utils.CcpDbRequester;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.exceptions.db.CcpEntityRecordNotFound;

public class CcpSelectUnionAll {

	public final CcpJsonRepresentation  condensed;

	public CcpSelectUnionAll(List<CcpJsonRepresentation> results) {
		CcpJsonRepresentation  condensed = CcpConstants.EMPTY_JSON;
		CcpDbRequester dependency = CcpDependencyInjection.getDependency(CcpDbRequester.class);
		
		String fieldNameToEntity = dependency.getFieldNameToEntity();
		String fieldNameToId = dependency.getFieldNameToId();
		
		for (CcpJsonRepresentation result : results) {
			String id = result.getAsString(fieldNameToId);
			String index = result.getAsString(fieldNameToEntity);
			CcpJsonRepresentation removeKeys = result.removeKeys(fieldNameToId, fieldNameToEntity);
			condensed = condensed.putSubKey(index, id, removeKeys);
		}
		this.condensed = condensed;
	}
	
	public boolean isPresent(String entityName, String id) {
		
		boolean entityNotFound = this.condensed.containsAllKeys(entityName) == false;
		
		if(entityNotFound) {
			return false;
		}
		CcpJsonRepresentation innerJson = this.condensed.getInnerJsonFromPath(entityName, id);
		
		boolean idNotFound = innerJson.isEmpty();
		
		if(idNotFound) {
			return false;
		}
		return true;
	}
	
	public <T> T handleRecordInUnionAll(
			CcpJsonRepresentation searchParameter, 
			HandleWithSearchResultsInTheEntity<T> handler
			) {
		
		CcpEntity entity = handler.getEntityToSearch();
	
		boolean recordNotFound = entity.isPresentInThisUnionAll(this, searchParameter) == false;
		
		if(recordNotFound) {
			T whenRecordWasNotFoundInTheEntitySearch = handler.whenRecordWasNotFoundInTheEntitySearch(searchParameter);
			return whenRecordWasNotFoundInTheEntitySearch;
		}
		
		CcpJsonRepresentation recordFound = this.getRequiredEntityRow(entity, searchParameter);
		
		T whenRecordWasFoundInTheEntitySearch = handler.whenRecordWasFoundInTheEntitySearch(searchParameter, recordFound);
		
		return whenRecordWasFoundInTheEntitySearch;
	}
	
	
	public CcpJsonRepresentation getRequiredEntityRow(CcpEntity entity, CcpJsonRepresentation json) {
		
		boolean notFound = entity.isPresentInThisUnionAll(this, json) == false;

		if(notFound) {
			throw new CcpEntityRecordNotFound(entity, json);
		}
		
		CcpJsonRepresentation entityRow = entity.getRecordFromUnionAll(this, json);
		
		return entityRow;
	}
	
	
	public CcpJsonRepresentation getEntityRow(String index, String id) {
		
		boolean indexNotFound = this.condensed.containsAllKeys(index) == false;
		
		if(indexNotFound) {
			return CcpConstants.EMPTY_JSON;
		}
		
		CcpJsonRepresentation innerJson = this.condensed.getInnerJson(index);

		boolean idNotFound = innerJson.containsAllKeys(id) == false;
		
		if(idNotFound) {
			return CcpConstants.EMPTY_JSON;
		}
		
		CcpJsonRepresentation jsonValue = innerJson.getInnerJson(id);
		return jsonValue;
	}

	public String toString() {
		return this.condensed.toString();
	}
	
	
}

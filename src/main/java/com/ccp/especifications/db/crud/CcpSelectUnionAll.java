package com.ccp.especifications.db.crud;

import java.util.List;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.exceptions.db.CcpEntityRecordNotFound;

public class CcpSelectUnionAll {

	public final CcpJsonRepresentation  condensed;

	public CcpSelectUnionAll(List<CcpJsonRepresentation> results) {
		
		CcpJsonRepresentation  condensed = CcpConstants.EMPTY_JSON;
		
		for (CcpJsonRepresentation result : results) {
			String index = result.getAsString("_index");
			String id = result.getAsString("_id");
			CcpJsonRepresentation removeKeys = result.removeKeys("_index", "_id");
			condensed = condensed.putSubKey(index, id, removeKeys);
		}
		
		this.condensed = condensed;
	}
	
	public boolean isPresent(CcpEntity entity, CcpJsonRepresentation value) {
		
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
	
		boolean recordNotFound = this.isPresent(entity, searchParameter) == false;
		
		if(recordNotFound) {
			T whenRecordWasNotFoundInTheEntitySearch = handler.whenRecordWasNotFoundInTheEntitySearch(searchParameter);
			return whenRecordWasNotFoundInTheEntitySearch;
		}
		
		CcpJsonRepresentation recordFound = this.getRequiredEntityRow(entity, searchParameter);
		
		T whenRecordWasFoundInTheEntitySearch = handler.whenRecordWasFoundInTheEntitySearch(searchParameter, recordFound);
		
		return whenRecordWasFoundInTheEntitySearch;
	}
	
	
	public CcpJsonRepresentation getEntityRow(CcpEntity entity, CcpJsonRepresentation value) {
		
		String index = entity.getEntityName();
		String id = entity.getId(value);
		
		CcpJsonRepresentation jsonValue = this.getEntityRow(index, id);
		return jsonValue;
	}

	public CcpJsonRepresentation getRequiredEntityRow(CcpEntity entity, CcpJsonRepresentation value) {
		
		CcpJsonRepresentation entityRow = this.getEntityRow(entity, value);
		
		boolean notFound = entityRow.isEmpty();
		
		if(notFound) {
			throw new CcpEntityRecordNotFound(entity, value);
		}
		
		return entityRow;
	}
	
	
	private CcpJsonRepresentation getEntityRow(String index, String id) {
		
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

package com.ccp.especifications.db.crud;

import java.util.List;
import java.util.function.Function;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.db.utils.CcpDbRequester;
import com.ccp.especifications.db.utils.CcpEntity;

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
			CcpJsonRepresentation removeKeys = result.removeFields(fieldNameToId, fieldNameToEntity);
			condensed = condensed.addToItem(index, id, removeKeys);
		}
		this.condensed = condensed;
	}
	
	public boolean isPresent(String entityName, String id) {
		
		boolean entityNotFound = this.condensed.containsAllFields(entityName) == false;
		
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
			Function<CcpJsonRepresentation, CcpJsonRepresentation> doBeforeSavingIfRecordIsNotFound = handler.doBeforeSavingIfRecordIsNotFound();
			CcpJsonRepresentation apply = doBeforeSavingIfRecordIsNotFound.apply(searchParameter);
			T whenRecordWasNotFoundInTheEntitySearch = handler.whenRecordWasNotFoundInTheEntitySearch(apply);
			return whenRecordWasNotFoundInTheEntitySearch;
		}
		
		CcpJsonRepresentation recordFound = entity.getRequiredEntityRow(this, searchParameter);
		
		Function<CcpJsonRepresentation, CcpJsonRepresentation> doBeforeSavingIfRecordIsFound = handler.doBeforeSavingIfRecordIsFound();
		
		CcpJsonRepresentation apply = doBeforeSavingIfRecordIsFound.apply(searchParameter);
		
		T whenRecordWasFoundInTheEntitySearch = handler.whenRecordWasFoundInTheEntitySearch(apply, recordFound);
		
		return whenRecordWasFoundInTheEntitySearch;
	}
	
	public CcpJsonRepresentation getEntityRow(String index, String id) {
		
		boolean indexNotFound = this.condensed.containsAllFields(index) == false;
		
		if(indexNotFound) {
			return CcpConstants.EMPTY_JSON;
		}
		
		CcpJsonRepresentation innerJson = this.condensed.getInnerJson(index);

		boolean idNotFound = innerJson.containsAllFields(id) == false;
		
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

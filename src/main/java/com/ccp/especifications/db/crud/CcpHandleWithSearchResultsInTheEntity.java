package com.ccp.especifications.db.crud;

import java.util.function.Function;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.CcpEntity;

public interface CcpHandleWithSearchResultsInTheEntity<T> {

	T whenRecordWasFoundInTheEntitySearch(CcpJsonRepresentation searchParameter, CcpJsonRepresentation recordFound);

	T whenRecordWasNotFoundInTheEntitySearch(CcpJsonRepresentation searchParameter);
	
	CcpEntity getEntityToSearch();
	
	default Function<CcpJsonRepresentation, CcpJsonRepresentation> doBeforeSavingIfRecordIsNotFound(){
		return CcpConstants.DO_NOTHING;
	}

	default Function<CcpJsonRepresentation, CcpJsonRepresentation> doBeforeSavingIfRecordIsFound(){
		return CcpConstants.DO_NOTHING;
	}

	default Function<CcpJsonRepresentation, CcpJsonRepresentation> doAfterSavingIfRecordIsNotFound(){
		return CcpConstants.DO_NOTHING;
	}

	default Function<CcpJsonRepresentation, CcpJsonRepresentation> doAfterSavingIfRecordIsFound(){
		return CcpConstants.DO_NOTHING;
	}
	
}

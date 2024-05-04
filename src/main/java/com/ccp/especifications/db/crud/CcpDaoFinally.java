package com.ccp.especifications.db.crud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.exceptions.process.CcpFlow;
import com.ccp.process.CcpProcessStatus;


public class CcpDaoFinally {
	private final CcpJsonRepresentation id;
	private final CcpJsonRepresentation statements;
	private final String[] fields;
	CcpDaoFinally(CcpJsonRepresentation id, CcpJsonRepresentation statements, String[] fields) {
		this.id = id;
		this.fields = fields;
		this.statements = statements;

	}

	public void endThisProcedure() {
		List<CcpJsonRepresentation> statements = this.statements.getAsJsonList("statements");
		CcpJsonRepresentation[] array = statements.toArray(new CcpJsonRepresentation[statements.size()]);
		this.findById(this.id, array);
	}

	public CcpJsonRepresentation endThisProcedureRetrievingTheResultingData() {
		List<CcpJsonRepresentation> statements = this.statements.getAsJsonList("statements");
		CcpJsonRepresentation[] array = statements.toArray(new CcpJsonRepresentation[statements.size()]);
		CcpJsonRepresentation findById = this.findById(this.id, array);
		return findById;
	}

	
	private CcpJsonRepresentation findById(CcpJsonRepresentation values, CcpJsonRepresentation... specifications) {
		int counter = 0;
		List<CcpEntity> keySet = Arrays.asList(specifications).stream()
				.filter(x -> x.getAsObject("entity") != null)
				.map(x -> (CcpEntity) x.getAsObject("entity") )
				.collect(Collectors.toList());
		
		LinkedHashSet<CcpEntity> set = new LinkedHashSet<>(keySet);

		CcpEntity[]  entities = new CcpEntity[set.size()];
		
		for (CcpEntity ccpDbEntity : set) { 
			entities[counter++] = ccpDbEntity;
		}

		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		List<CcpJsonRepresentation> manyById = crud.getManyById(values, entities);
		counter = 0;
		
		for (CcpJsonRepresentation specification : specifications) {
			
			String entity = specification.getAsString("entity");
			
			boolean executeFreeAction = entity.trim().isEmpty();
			
			if(executeFreeAction) {
				Function<CcpJsonRepresentation, CcpJsonRepresentation> action = specification.getAsObject("action");
				values = action.apply(values);
				continue;
			}
			
			boolean recordFound = specification.getAsBoolean("found");
			
			Optional<CcpJsonRepresentation> findFirst = new ArrayList<>(manyById).stream()
					.filter(x -> x.getAsString("_index").equals(entity))
					.findFirst();

			CcpJsonRepresentation dataBaseRow = findFirst.get();
			
			boolean _found = dataBaseRow.getAsBoolean("_found");
			boolean itWasNotForeseen = _found != recordFound;
			
			if(itWasNotForeseen) {

				if(_found == false) {
					continue;
				}

				values = values.putSubKey("_entities", entity, dataBaseRow);
				
				continue;
			}
			
			
			boolean willNotExecuteAction = specification.containsKey("action") == false;
			
			if(willNotExecuteAction) {
			
				boolean willNotThrowException = specification.containsKey("status") == false;
				
				if(willNotThrowException) {
					continue;
				}
				
				CcpProcessStatus status = specification.getAsObject("status");
				String message = specification.getOrDefault("message", status.name());
				throw new CcpFlow(values, status.status() , message, this.fields);
			}
			
			Function<CcpJsonRepresentation, CcpJsonRepresentation> action = specification.getAsObject("action");

			if(recordFound == false) {
				values = action.apply(values);
				continue;
			}
			
			CcpJsonRepresentation context = values.putSubKey("_entities", entity, dataBaseRow);
			values = action.apply(context);
		}
		
		if(this.fields.length <= 0) {
			return CcpConstants.EMPTY_JSON;
		}
		
		CcpJsonRepresentation subMap = values.getJsonPiece(this.fields);
		return subMap;
	}

	
}

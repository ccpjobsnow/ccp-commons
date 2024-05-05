package com.ccp.especifications.db.crud;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.exceptions.process.CcpFlow;
import com.ccp.process.CcpProcessStatus;


public class CcpSelectFinally {
	private final CcpJsonRepresentation id;
	private final CcpJsonRepresentation statements;
	private final String[] fields;
	CcpSelectFinally(CcpJsonRepresentation id, CcpJsonRepresentation statements, String[] fields) {
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
		List<CcpEntity> keySet = Arrays.asList(specifications).stream()
				.filter(x -> x.getAsObject("entity") != null)
				.map(x -> (CcpEntity) x.get("entity") )
				.collect(Collectors.toList());
		
		LinkedHashSet<CcpEntity> set = new LinkedHashSet<>(keySet);

		CcpEntity[] entities = set.toArray(new CcpEntity[set.size()]);
		
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		
		CcpSelectUnionAll unionAll = crud.unionAll(values, entities);
		
		for (CcpJsonRepresentation specification : specifications) {
			
			boolean executeFreeAction = specification.containsKey("entity") == false;
			
			if(executeFreeAction) {
				Function<CcpJsonRepresentation, CcpJsonRepresentation> action = specification.getAsObject("action");
				values = action.apply(values);
				continue;
			}
			
			boolean shouldHaveBeenFound = specification.getAsBoolean("found");
			
			CcpEntity entity = specification.getAsObject("entity");
			
			boolean wasActuallyFound = unionAll.isPresent(entity, values);
			
			boolean itWasNotForeseen = wasActuallyFound != shouldHaveBeenFound;
			
			if(itWasNotForeseen) {

				if(wasActuallyFound == false) {
					continue;
				}
				String entityName = entity.getEntityName();
				CcpJsonRepresentation dataBaseRow = unionAll.getEntityRow(entity,values);
				values = values.putSubKey("_entities", entityName, dataBaseRow);
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

			if(shouldHaveBeenFound == false) {
				values = action.apply(values);
				continue;
			}
			String entityName = entity.getEntityName();
			CcpJsonRepresentation dataBaseRow = unionAll.getEntityRow(entity,values);
			CcpJsonRepresentation context = values.putSubKey("_entities", entityName, dataBaseRow);
			values = action.apply(context);
		}
		
		if(this.fields.length <= 0) {
			return CcpConstants.EMPTY_JSON;
		}
		
		CcpJsonRepresentation subMap = values.getJsonPiece(this.fields);
		return subMap;
	}
}

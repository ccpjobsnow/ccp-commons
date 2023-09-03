package com.ccp.especifications.db.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.exceptions.commons.CcpFlow;
import com.ccp.process.CcpProcessStatus;


public class Finally {
	private final CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);
	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;
	
	Finally(CcpMapDecorator id, CcpMapDecorator statements) {
		this.id = id;
		this.statements = statements;
	}

	public void endThisProcedure() {
		List<CcpMapDecorator> statements = this.statements.getAsMapList("statements");
		CcpMapDecorator[] array = statements.toArray(new CcpMapDecorator[statements.size()]);
		this.findById(this.id, array);
	}

	public CcpMapDecorator endThisProcedureRetrievingTheResultingData() {
		List<CcpMapDecorator> statements = this.statements.getAsMapList("statements");
		CcpMapDecorator[] array = statements.toArray(new CcpMapDecorator[statements.size()]);
		CcpMapDecorator findById = this.findById(this.id, array);
		return findById;
	}

	
	private CcpMapDecorator findById(CcpMapDecorator values, CcpMapDecorator... specifications) {
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

		List<CcpMapDecorator> manyById = this.dao.getManyById(values, entities);
		counter = 0;
		
		for (CcpMapDecorator specification : specifications) {
			
			String entity = specification.getAsString("entity");
			
			boolean executeFreeAction = entity.trim().isEmpty();
			
			if(executeFreeAction) {
				try {
					Function<CcpMapDecorator, CcpMapDecorator> action = specification.getAsObject("action");
					values = action.apply(values);
					continue;
					
				}catch (CcpFlow e) {
					throw e;
				} 
				catch (Exception e) {
					continue;
				}
			}
			
			boolean recordFound = specification.getAsBoolean("found");
			
			Optional<CcpMapDecorator> findFirst = new ArrayList<>(manyById).stream()
					.filter(x -> x.getAsString("_index").equals(entity))
					.findFirst();

			CcpMapDecorator dataBaseRow = findFirst.get();
			
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
				String message = specification.getAsString("message");
				throw new CcpFlow(new CcpMapDecorator().put("message", status.name()), status.status() , message);
			}
			
			Function<CcpMapDecorator, CcpMapDecorator> action = specification.getAsObject("action");

			if(recordFound == false) {
				values = action.apply(values);
				continue;
			}
			
			CcpMapDecorator context = values.putSubKey("_entities", entity, dataBaseRow);
			values = action.apply(context);
		}
		
		return values;
	}

	
}

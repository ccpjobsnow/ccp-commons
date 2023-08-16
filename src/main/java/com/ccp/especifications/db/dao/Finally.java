package com.ccp.especifications.db.dao;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.exceptions.commons.CcpFlow;


public class Finally {
	private final CcpDao dao;
	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;
	
	Finally(CcpDao dao, CcpMapDecorator id, CcpMapDecorator statements) {
		this.id = id;
		this.dao = dao;
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

	
	private CcpMapDecorator findById(CcpMapDecorator values, CcpMapDecorator... roadMap) {
		CcpEntity[]  entities = new CcpEntity[roadMap.length];
		int k = 0;
		List<CcpEntity> keySet = Arrays.asList(roadMap).stream().map(x -> (CcpEntity) x.getAsObject("entity") ).collect(Collectors.toList());
		LinkedHashSet<CcpEntity> set = new LinkedHashSet<>(keySet);
		for (CcpEntity ccpDbEntity : set) {
			entities[k++] = ccpDbEntity;
		}

		List<CcpMapDecorator> manyById = this.dao.getManyById(values, entities);
		k = 0;
		
		for (CcpMapDecorator dataBaseRow : manyById) {
			String entity = dataBaseRow.getAsString("_index");

			boolean recordFound = dataBaseRow.getAsBoolean("_found");
			
			Optional<CcpMapDecorator> findFirst = Arrays.asList(roadMap).stream()
					.filter(x -> x.getAsString("entity").equals(entity))
					.filter(x -> x.getAsBoolean("found") == recordFound)
					.findFirst();

			
			boolean itWasNotForeseen = findFirst.isPresent() == false;
			
			if(itWasNotForeseen) {

				if(recordFound == false) {
					continue;
				}
				
				values = values.putSubKey("_entities", entity, dataBaseRow);
				
				continue;
			}
			
			CcpMapDecorator specification = findFirst.get();
			
			boolean willNotExecuteAction = specification.containsKey("action") == false;
			
			if(willNotExecuteAction) {
			
				boolean willNotThrowException = specification.containsKey("status") == false;
				
				if(willNotThrowException) {
					continue;
				}
				
				Integer status = specification.getAsIntegerNumber("status");
				String message = specification.getAsString("message");
				throw new CcpFlow(values.put("_dataBaseRow", dataBaseRow), status , message);
			}
			
			Function<CcpMapDecorator, CcpMapDecorator> action = specification.getAsObject("action");

			if(recordFound == false) {
				CcpMapDecorator execute = action.apply(values);
				return execute;
			}
			
			CcpMapDecorator context = values.putSubKey("_entities", entity, dataBaseRow);
			CcpMapDecorator execute = action.apply(context);
			return execute;
		}
		
		return values;
	}

	
}

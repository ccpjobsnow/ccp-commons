package com.ccp.especifications.db.crud;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpDbTable;
import com.ccp.exceptions.commons.CcpFlow;
import com.ccp.process.CcpProcess;

public class Finally {
	private final CcpDbCrud crud;
	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;
	
	Finally(CcpDbCrud crud, CcpMapDecorator id, CcpMapDecorator statements) {
		this.id = id;
		this.crud = crud;
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
		CcpDbTable[]  tables = new CcpDbTable[roadMap.length];
		int k = 0;
		List<CcpDbTable> keySet = Arrays.asList(roadMap).stream().map(x -> (CcpDbTable) x.getAsObject("table") ).collect(Collectors.toList());
		LinkedHashSet<CcpDbTable> set = new LinkedHashSet<>(keySet);
		for (CcpDbTable ccpDbTable : set) {
			tables[k++] = ccpDbTable;
		}

		List<CcpMapDecorator> manyById = this.crud.getManyById(values, tables);
		k = 0;
		
		for (CcpMapDecorator dataBaseRow : manyById) {
			String tableName = dataBaseRow.getAsString("_index");

			boolean recordFound = dataBaseRow.getAsBoolean("_found");
			
			Optional<CcpMapDecorator> findFirst = Arrays.asList(roadMap).stream()
					.filter(x -> x.getAsString("table").equals(tableName))
					.filter(x -> x.getAsBoolean("_found") == recordFound)
					.findFirst();

			CcpMapDecorator record = dataBaseRow.getInternalMap("_source");
			
			boolean itWasNotForeseen = findFirst.isPresent() == false;
			
			if(itWasNotForeseen) {

				if(recordFound == false) {
					continue;
				}
				
				values = values.putSubKey("_tables", tableName, record);
				
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
			
			CcpProcess action = specification.getAsObject("action");

			if(recordFound == false) {
				CcpMapDecorator execute = action.execute(values);
				return execute;
			}
			
			CcpMapDecorator context = values.putSubKey("_tables", tableName, record);
			CcpMapDecorator execute = action.execute(context);
			return execute;
		}
		
		return values;
	}

	
}

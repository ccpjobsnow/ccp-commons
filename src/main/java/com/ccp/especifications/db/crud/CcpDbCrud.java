package com.ccp.especifications.db.crud;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.table.CcpDbTable;
import com.ccp.exceptions.commons.Flow;
import com.ccp.process.CcpProcess;

public interface CcpDbCrud {
	
	Set<String> getSynonyms(Set<String> wordsToAnalyze, CcpDbTable tableName,  String... analyzers);
	List<CcpMapDecorator> getManyByIds(CcpMapDecorator filterEspecifications);
	boolean updateOrSave(CcpMapDecorator data, CcpDbTable tableName, String id);
	List<CcpMapDecorator> getManyByIds(CcpDbTable tableName, String... ids);
	List<CcpMapDecorator> getManyById(String id, CcpDbTable... tables);
	CcpMapDecorator getOneById(CcpDbTable tableName, String id);
	boolean exists(CcpDbTable tableName, String id);
	
	default CcpMapDecorator findById(CcpMapDecorator values, String id, CcpMapDecorator...roadMap) {
		CcpDbTable[]  tables = new CcpDbTable[roadMap.length];
		int k = 0;
		List<CcpDbTable> keySet = Arrays.asList(roadMap).stream().map(x -> (CcpDbTable) x.getAsObject("table") ).collect(Collectors.toList());
		for (CcpDbTable ccpDbTable : keySet) {
			tables[k++] = ccpDbTable;
		}

		List<CcpMapDecorator> manyById = this.getManyById(id, tables);
		
		k = 0;
		
		for (CcpMapDecorator dataBaseRow : manyById) {
		
			String tableName = dataBaseRow.getAsString("_index");

			boolean found = dataBaseRow.getAsBoolean("found");
			
			CcpMapDecorator spec = Arrays.asList(roadMap).stream()
					.filter(x -> x.getAsString("table").equals(tableName))
					.filter(x -> x.getAsBoolean("found") == found)
					.findFirst().get();
			
			if(spec.containsKey("action")) {
				CcpMapDecorator record = dataBaseRow.getInternalMap("_source");
				CcpMapDecorator context = new CcpMapDecorator().put("record", record).put("values", values);
				CcpProcess action = spec.getAsObject("action");
				CcpMapDecorator execute = action.execute(context);
				return execute;
			}
			
			Integer status = spec.getAsIntegerNumber("status");
			String message = spec.getAsString("message");
			throw new Flow(values, status , message);
		}
		return values;
	}
}

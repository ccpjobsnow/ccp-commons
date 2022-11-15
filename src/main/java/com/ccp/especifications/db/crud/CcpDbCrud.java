package com.ccp.especifications.db.crud;

import java.util.List;
import java.util.Set;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.table.CcpDbTable;

public interface CcpDbCrud {
	
	Set<String> getSynonyms(Set<String> wordsToAnalyze, CcpDbTable tableName,  String... analyzers);
	List<CcpMapDecorator> getManyById(CcpMapDecorator values, CcpDbTable... tables);
	CcpMapDecorator findById(CcpMapDecorator values, CcpMapDecorator...roadMap) ;
	boolean updateOrSave(CcpMapDecorator data, CcpDbTable tableName, String id);
	List<CcpMapDecorator> getManyByIds(CcpMapDecorator filterEspecifications);
	List<CcpMapDecorator> getManyByIds(CcpDbTable tableName, String... ids);
	CcpMapDecorator getOneById(CcpDbTable tableName, String id);
	boolean exists(CcpDbTable tableName, String id);
	CcpMapDecorator remove(String id);
}

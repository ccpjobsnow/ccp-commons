package com.ccp.especifications.db.crud;

import java.util.List;
import java.util.Set;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpDbCrud {
	
	Set<String> getSynonyms(Set<String> wordsToAnalyze, String tableName,  String... analyzers);
	List<CcpMapDecorator> getManyByIds(CcpMapDecorator filterEspecifications);
	boolean updateOrSave(CcpMapDecorator data, String id, String tableName);
	List<CcpMapDecorator> getManyByIds(String[] ids, String tableName);
	List<CcpMapDecorator> getManyById(String id, String... tables);
	CcpMapDecorator getOneById(String id, String tableName);
	boolean exists(String id, String tableName);
}

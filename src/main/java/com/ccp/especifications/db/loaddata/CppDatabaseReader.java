package com.ccp.especifications.db.loaddata;

import java.util.List;
import java.util.Map;

import com.ccp.decorators.CcpMapDecorator;

public interface CppDatabaseReader {
	
	List<CcpMapDecorator> getManyByIds(Map<String, String> filterEspecifications);
	List<CcpMapDecorator> getManyByIds(String[] ids, String... tables);
	List<CcpMapDecorator> getManyById(String id, String... tables);
	CcpMapDecorator getOneById(String id, String tableName);
	
}

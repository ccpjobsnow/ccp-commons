package com.ccp.especifications.db.crud;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpDbCrud {
	
	List<CcpMapDecorator> getManyByIds(CcpMapDecorator filterEspecifications);
	List<CcpMapDecorator> getManyByIds(String[] ids, String tableName);
	List<CcpMapDecorator> getManyById(String id, String... tables);
	CcpMapDecorator getOneById(String id, String tableName);
	
}

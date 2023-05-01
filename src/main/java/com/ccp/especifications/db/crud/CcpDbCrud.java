package com.ccp.especifications.db.crud;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpDbTable;

public interface CcpDbCrud {
	
	List<CcpMapDecorator> getManyById(CcpMapDecorator values, CcpDbTable... tables);
	boolean updateOrSave(CcpMapDecorator data, CcpDbTable tableName, String id);
	List<CcpMapDecorator> getManyByIds(CcpMapDecorator filterEspecifications);
	List<CcpMapDecorator> getManyByIds(CcpDbTable tableName, String... ids);
	CcpMapDecorator getOneById(CcpDbTable tableName, String id);
	boolean exists(CcpDbTable tableName, String id);
	CcpMapDecorator remove(String id);
	default UseThisId useThisId(CcpMapDecorator id) {
		return new UseThisId(id, new CcpMapDecorator(), this);
	}

}

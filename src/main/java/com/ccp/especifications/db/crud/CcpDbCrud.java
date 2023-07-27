package com.ccp.especifications.db.crud;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpDbTable;

public interface CcpDbCrud {

	List<CcpMapDecorator> getManyById(List<CcpMapDecorator> values, CcpDbTable... tables);
	
	List<CcpMapDecorator> getManyById(CcpMapDecorator values, CcpDbTable... tables);
	
	boolean updateOrSave(CcpDbTable tableName, CcpMapDecorator data);
	
	List<CcpMapDecorator> getManyByIds(CcpDbTable tableName, String... ids);
	
	CcpMapDecorator getOneById(CcpDbTable tableName, CcpMapDecorator values);
	
	boolean exists(CcpDbTable tableName, CcpMapDecorator values);
	
	CcpMapDecorator remove(CcpDbTable tableName, CcpMapDecorator values);
	
	default UseThisId useThisId(CcpMapDecorator id) {
		return new UseThisId(id, new CcpMapDecorator(), this);
	}
	
	default boolean anyMatch(CcpMapDecorator values, CcpDbTable... tables) {
		List<CcpMapDecorator> manyById = this.getManyById(values, tables);
		for (CcpMapDecorator md : manyById) {
			boolean found = md.getAsBoolean("_found");
			
			if(found) {
				return true;
			}
		}
		return false;
	}

	default boolean allMatch(CcpMapDecorator values, CcpDbTable... tables) {
		List<CcpMapDecorator> manyById = this.getManyById(values, tables);
		for (CcpMapDecorator md : manyById) {
			boolean notFound = md.getAsBoolean("_found") == false;
			
			if(notFound) {
				return false;
			}
		}
		return true;
	}

	default boolean noMatches(CcpMapDecorator values, CcpDbTable... tables) {
		List<CcpMapDecorator> manyById = this.getManyById(values, tables);
		for (CcpMapDecorator md : manyById) {
			boolean found = md.getAsBoolean("_found");
			
			if(found) {
				return false;
			}
		}
		return true;
	}

}

package com.ccp.especifications.db.crud;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpEntity;

public interface CcpDao {

	List<CcpMapDecorator> getManyById(List<CcpMapDecorator> values, CcpEntity... tables);
	
	List<CcpMapDecorator> getManyById(CcpMapDecorator values, CcpEntity... tables);
	
	CcpMapDecorator createOrUpdate(CcpEntity tableName, CcpMapDecorator data);
	
	List<CcpMapDecorator> getManyByIds(CcpEntity tableName, String... ids);
	
	CcpMapDecorator getOneById(CcpEntity tableName, CcpMapDecorator values);
	
	boolean exists(CcpEntity tableName, CcpMapDecorator values);
	
	CcpMapDecorator delete(CcpEntity tableName, CcpMapDecorator values);
	
	default UseThisId useThisId(CcpMapDecorator id) {
		return new UseThisId(id, new CcpMapDecorator(), this);
	}
	
	default boolean anyMatch(CcpMapDecorator values, CcpEntity... tables) {
		List<CcpMapDecorator> manyById = this.getManyById(values, tables);
		for (CcpMapDecorator md : manyById) {
			boolean found = md.getAsBoolean("_found");
			
			if(found) {
				return true;
			}
		}
		return false;
	}

	default boolean allMatch(CcpMapDecorator values, CcpEntity... tables) {
		List<CcpMapDecorator> manyById = this.getManyById(values, tables);
		for (CcpMapDecorator md : manyById) {
			boolean notFound = md.getAsBoolean("_found") == false;
			
			if(notFound) {
				return false;
			}
		}
		return true;
	}

	default boolean noMatches(CcpMapDecorator values, CcpEntity... tables) {
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

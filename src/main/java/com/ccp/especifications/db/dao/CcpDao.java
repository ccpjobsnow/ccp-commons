package com.ccp.especifications.db.dao;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpEntity;

public interface CcpDao {

	List<CcpMapDecorator> getManyById(CcpMapDecorator values, CcpEntity... entities);
	
	CcpMapDecorator createOrUpdate(CcpEntity entity, CcpMapDecorator data);
	
	List<CcpMapDecorator> getManyByIds(CcpEntity entity, String... ids);
	
	boolean exists(CcpEntity entity, CcpMapDecorator values);
	
	CcpMapDecorator delete(CcpEntity entity, CcpMapDecorator values);
	
	default UseThisId useThisId(CcpMapDecorator id) {
		return new UseThisId(id, new CcpMapDecorator(), this);
	}
	
	default boolean anyMatch(CcpMapDecorator values, CcpEntity... entities) {
		List<CcpMapDecorator> manyById = this.getManyById(values, entities);
		for (CcpMapDecorator md : manyById) {
			boolean found = md.getAsBoolean("_found");
			
			if(found) {
				return true;
			}
		}
		return false;
	}

	default boolean allMatch(CcpMapDecorator values, CcpEntity... entities) {
		List<CcpMapDecorator> manyById = this.getManyById(values, entities);
		for (CcpMapDecorator md : manyById) {
			boolean notFound = md.getAsBoolean("_found") == false;
			
			if(notFound) {
				return false;
			}
		}
		return true;
	}

	default boolean noMatches(CcpMapDecorator values, CcpEntity... entities) {
		List<CcpMapDecorator> manyById = this.getManyById(values, entities);
		for (CcpMapDecorator md : manyById) {
			boolean found = md.getAsBoolean("_found");
			
			if(found) {
				return false;
			}
		}
		return true;
	}

	CcpMapDecorator getOneById(CcpEntity entity, String id);

	default CcpMapDecorator getOneById(CcpEntity entity, CcpMapDecorator values) {
	
		String id = entity.getId(values);
		
		CcpMapDecorator oneById = this.getOneById(entity, id);
		
		return oneById;
	}

	CcpMapDecorator getAllData(CcpMapDecorator values, CcpEntity... entities);

	List<CcpMapDecorator> getManyById(List<CcpMapDecorator> values, CcpEntity... entities);

	CcpMapDecorator createOrUpdate(CcpEntity entity, CcpMapDecorator data, String id); 
}

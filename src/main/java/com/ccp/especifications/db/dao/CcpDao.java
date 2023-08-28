package com.ccp.especifications.db.dao;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpIdGenerator;

public interface CcpDao {

	List<CcpMapDecorator> getManyById(CcpMapDecorator values, CcpIdGenerator... entities);
	
	default CcpMapDecorator createOrUpdate(CcpIdGenerator entity, CcpMapDecorator data) {
		
		String id = entity.getId(data);

		CcpMapDecorator response = this.createOrUpdate(entity, data, id);
		
		return response;
	}
	
	List<CcpMapDecorator> getManyByIds(CcpIdGenerator entity, String... ids);
	
	default boolean exists(CcpIdGenerator entity, CcpMapDecorator values) {
		String id = entity.getId(values);
		
		boolean exists = this.exists(entity, id);
		return exists;
	}
	
	default boolean anyMatch(CcpMapDecorator values, CcpIdGenerator... entities) {
		List<CcpMapDecorator> manyById = this.getManyById(values, entities);
		for (CcpMapDecorator md : manyById) {
			boolean found = md.getAsBoolean("_found");
			
			if(found) {
				return true;
			}
		}
		return false;
	}

	default boolean allMatch(CcpMapDecorator values, CcpIdGenerator... entities) {
		List<CcpMapDecorator> manyById = this.getManyById(values, entities);
		for (CcpMapDecorator md : manyById) {
			boolean notFound = md.getAsBoolean("_found") == false;
			
			if(notFound) {
				return false;
			}
		}
		return true;
	}

	default boolean noMatches(CcpMapDecorator values, CcpIdGenerator... entities) {
		List<CcpMapDecorator> manyById = this.getManyById(values, entities);
		for (CcpMapDecorator md : manyById) {
			boolean found = md.getAsBoolean("_found");
			
			if(found) {
				return false;
			}
		}
		return true;
	}

	CcpMapDecorator getOneById(CcpIdGenerator entity, String id);

	default CcpMapDecorator getOneById(CcpIdGenerator entity, CcpMapDecorator values) {
	
		String id = entity.getId(values);
		
		CcpMapDecorator oneById = this.getOneById(entity, id);
		
		return oneById;
	}

	CcpMapDecorator getAllData(CcpMapDecorator values, CcpIdGenerator... entities);

	List<CcpMapDecorator> getManyById(List<CcpMapDecorator> values, CcpIdGenerator... entities);

	CcpMapDecorator createOrUpdate(CcpIdGenerator entity, CcpMapDecorator data, String id);

	boolean exists(CcpIdGenerator entity, String id);

	boolean delete(CcpIdGenerator entity, String id); 
	
	default boolean delete(CcpIdGenerator entity, CcpMapDecorator values) {
		String id = entity.getId(values);

		boolean deleted = this.delete(entity, id);
		return deleted;
	}

}

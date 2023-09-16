package com.ccp.especifications.db.dao;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpEntityIdGenerator;

public interface CcpDao {

	List<CcpMapDecorator> getManyById(CcpMapDecorator values, CcpEntityIdGenerator... entities);
	
	default CcpMapDecorator createOrUpdate(CcpEntityIdGenerator entity, CcpMapDecorator data) {
		
		String id = entity.getId(data);

		CcpMapDecorator response = this.createOrUpdate(entity, data, id);
		
		return response;
	}
	
	List<CcpMapDecorator> getManyByIds(CcpEntityIdGenerator entity, String... ids);
	
	default boolean exists(CcpEntityIdGenerator entity, CcpMapDecorator values) {
		String id = entity.getId(values);
		
		boolean exists = this.exists(entity, id);
		return exists;
	}
	
	default boolean anyMatch(CcpMapDecorator values, CcpEntityIdGenerator... entities) {
		List<CcpMapDecorator> manyById = this.getManyById(values, entities);
		for (CcpMapDecorator md : manyById) {
			boolean found = md.getAsBoolean("_found");
			
			if(found) {
				return true;
			}
		}
		return false;
	}

	default boolean allMatch(CcpMapDecorator values, CcpEntityIdGenerator... entities) {
		List<CcpMapDecorator> manyById = this.getManyById(values, entities);
		for (CcpMapDecorator md : manyById) {
			boolean notFound = md.getAsBoolean("_found") == false;
			
			if(notFound) {
				return false;
			}
		}
		return true;
	}

	default boolean noMatches(CcpMapDecorator values, CcpEntityIdGenerator... entities) {
		List<CcpMapDecorator> manyById = this.getManyById(values, entities);
		for (CcpMapDecorator md : manyById) {
			boolean found = md.getAsBoolean("_found");
			
			if(found) {
				return false;
			}
		}
		return true;
	}

	CcpMapDecorator getOneById(CcpEntityIdGenerator entity, String id);

	default CcpMapDecorator getOneById(CcpEntityIdGenerator entity, CcpMapDecorator values) {
	
		String id = entity.getId(values);
		
		CcpMapDecorator oneById = this.getOneById(entity, id);
		
		return oneById;
	}

	CcpMapDecorator getAllData(CcpMapDecorator values, CcpEntityIdGenerator... entities);

	List<CcpMapDecorator> getManyById(List<CcpMapDecorator> values, CcpEntityIdGenerator... entities);

	CcpMapDecorator createOrUpdate(CcpEntityIdGenerator entity, CcpMapDecorator data, String id);

	boolean exists(CcpEntityIdGenerator entity, String id);

	boolean delete(CcpEntityIdGenerator entity, String id); 
	
	default boolean delete(CcpEntityIdGenerator entity, CcpMapDecorator values) {
		String id = entity.getId(values);

		boolean deleted = this.delete(entity, id);
		return deleted;
	}

}

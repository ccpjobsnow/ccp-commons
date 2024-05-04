package com.ccp.especifications.db.crud;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.CcpEntityIdGenerator;

public interface CcpCrud {

	List<CcpJsonRepresentation> getManyById(CcpJsonRepresentation values, CcpEntityIdGenerator... entities);
	
	default CcpJsonRepresentation createOrUpdate(CcpEntityIdGenerator entity, CcpJsonRepresentation data) {
		
		String id = entity.getId(data);

		CcpJsonRepresentation response = this.createOrUpdate(entity, data, id);
		
		return response;
	}
	
	List<CcpJsonRepresentation> getManyByIds(CcpEntityIdGenerator entity, String... ids);
	
	default boolean exists(CcpEntityIdGenerator entity, CcpJsonRepresentation values) {
		String id = entity.getId(values);
		
		boolean exists = this.exists(entity, id);
		return exists;
	}
	
	default boolean anyMatch(CcpJsonRepresentation values, CcpEntityIdGenerator... entities) {
		List<CcpJsonRepresentation> manyById = this.getManyById(values, entities);
		for (CcpJsonRepresentation md : manyById) {
			boolean found = md.getAsBoolean("_found");
			
			if(found) {
				return true;
			}
		}
		return false;
	}

	default boolean allMatch(CcpJsonRepresentation values, CcpEntityIdGenerator... entities) {
		List<CcpJsonRepresentation> manyById = this.getManyById(values, entities);
		for (CcpJsonRepresentation md : manyById) {
			boolean notFound = md.getAsBoolean("_found") == false;
			
			if(notFound) {
				return false;
			}
		}
		return true;
	}

	default boolean noMatches(CcpJsonRepresentation values, CcpEntityIdGenerator... entities) {
		List<CcpJsonRepresentation> manyById = this.getManyById(values, entities);
		for (CcpJsonRepresentation md : manyById) {
			boolean found = md.getAsBoolean("_found");
			
			if(found) {
				return false;
			}
		}
		return true;
	}

	CcpJsonRepresentation getOneById(CcpEntityIdGenerator entity, String id);

	default CcpJsonRepresentation getOneById(CcpEntityIdGenerator entity, CcpJsonRepresentation values) {
	
		String id = entity.getId(values);
		
		CcpJsonRepresentation oneById = this.getOneById(entity, id);
		
		return oneById;
	}
	
	CcpDaoUnionAll unionAll(Collection<CcpJsonRepresentation> values, CcpEntityIdGenerator... entities);

	CcpJsonRepresentation getAllData(CcpJsonRepresentation values, CcpEntityIdGenerator... entities);

	List<CcpJsonRepresentation> getManyById(List<CcpJsonRepresentation> values, CcpEntityIdGenerator... entities);

	CcpJsonRepresentation createOrUpdate(CcpEntityIdGenerator entity, CcpJsonRepresentation data, String id);

	boolean exists(CcpEntityIdGenerator entity, String id);

	boolean delete(CcpEntityIdGenerator entity, String id); 
	
	default boolean delete(CcpEntityIdGenerator entity, CcpJsonRepresentation values) {
		String id = entity.getId(values);

		boolean deleted = this.delete(entity, id);
		return deleted;
	}

	CcpDaoUnionAll unionAll(Set<String> values, CcpEntityIdGenerator... entities);

}

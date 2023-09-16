package com.ccp.especifications.db.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.db.dao.CcpDao;
import com.ccp.exceptions.db.CcpEntityRecordNotFound;
import com.ccp.exceptions.process.CcpFlow;


public interface CcpEntity extends CcpEntityIdGenerator{


	default CcpMapDecorator getOneById(CcpMapDecorator data, Function<CcpMapDecorator, CcpMapDecorator> ifNotFound) {
		try {
			CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);
			CcpMapDecorator oneById = dao.getOneById(this, data);
			return oneById;
			
		} catch (CcpEntityRecordNotFound e) {
			CcpMapDecorator execute = ifNotFound.apply(data);
			return execute;
		}
	}

	default CcpMapDecorator getOneById(CcpMapDecorator data) {
		CcpMapDecorator md = this.getOneById(data, x -> {throw new CcpFlow(x.put("entity", this.name()), 404);});
		return md;
	}
	

	default CcpMapDecorator getOneById(String id) {
		try {
			CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);
			CcpMapDecorator md = dao.getOneById(this, id);
			return md;
			
		} catch (CcpEntityRecordNotFound e) {
			CcpMapDecorator put = new CcpMapDecorator().put("id", id).put("entity", this.name());
			throw new CcpFlow(put, 404);
		}
	}
	
	default boolean exists(String id) {
		CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);
		boolean exists = dao.exists(this, id);
		return exists;
		
	}
	
	default boolean exists(CcpMapDecorator data) {
		CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);
		boolean exists = dao.exists(this, data);
		return exists;
	}
	
	CcpMapDecorator getOnlyExistingFields(CcpMapDecorator values) ;
	
	default CcpMapDecorator createOrUpdate(CcpMapDecorator values) {
		CcpMapDecorator onlyExistingFields = this.getOnlyExistingFields(values);
		boolean created = this.create(values);
		
		this.saveAuditory(values, created ? CcpEntityOperationType.create : CcpEntityOperationType.update);

		return onlyExistingFields;
	}

	default boolean create(CcpMapDecorator values) {
		CcpMapDecorator onlyExistingFields = this.getOnlyExistingFields(values);
		CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);

		CcpMapDecorator createOrUpdate = dao.createOrUpdate(this, onlyExistingFields);
		String result = createOrUpdate.getAsString("result");
		boolean created = "created".equals(result);
		
		return created;
	}
	

	default boolean delete(CcpMapDecorator values) {
		CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);
		boolean remove = dao.delete(this, values);
		this.saveAuditory(values, CcpEntityOperationType.delete);
		return remove;
	}
	
	default boolean delete(String id) {
		CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);
		boolean remove = dao.delete(this, id);
		//TODO SALVAR AUDITORIA
		return remove;
		
	}
	
	default List<CcpMapDecorator> getManyByIds(CcpMapDecorator... values){
		String[] ids = new String[values.length];
		
		int k = 0;
		
		for (CcpMapDecorator value : values) {
			String id = this.getId(value);
			ids[k++] = id;
		}
		CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);
		List<CcpMapDecorator> manyByIds = dao.getManyByIds(this, ids);
	
		k = 0;
		List<CcpMapDecorator> response = new ArrayList<>();
		for (CcpMapDecorator value : values) {
			CcpMapDecorator md = manyByIds.get(k++);
			md = md.put("_id", value);
			response.add(md);
		}
		return response;
	}
	
	default List<CcpMapDecorator> getManyByIds(List< CcpMapDecorator> values){
		CcpMapDecorator[] array = values.toArray(new CcpMapDecorator[values.size()]);
		return this.getManyByIds(array);
	}	
	void saveAuditory(CcpMapDecorator values, CcpEntityOperationType operation);
	
	boolean isAuditable();
	
	default CcpMapDecorator createOrUpdate(CcpMapDecorator data, String id) {
		CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);
		CcpMapDecorator createOrUpdate = dao.createOrUpdate(this, data, id);
		return createOrUpdate;
	}
	
	
}

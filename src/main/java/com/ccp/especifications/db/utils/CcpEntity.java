package com.ccp.especifications.db.utils;

import java.util.ArrayList;
import java.util.List;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.crud.CcpDao;
import com.ccp.exceptions.commons.CcpFlow;
import com.ccp.exceptions.db.CcpRecordNotFound;
import com.ccp.process.CcpProcess;

public interface CcpEntity {

	String name();
	
	String getId(CcpMapDecorator values);


	default CcpMapDecorator getOneById(CcpMapDecorator data, CcpProcess ifNotFound) {
		try {
			CcpDao crud = this.getDao();
			CcpMapDecorator oneById = crud.getOneById(this, data);
			return oneById;
			
		} catch (CcpRecordNotFound e) {
			CcpMapDecorator execute = ifNotFound.execute(data);
			return execute;
		}
	}

	default CcpMapDecorator getOneById(CcpMapDecorator data) {
		CcpMapDecorator md = this.getOneById(data, x -> {throw new CcpFlow(x, 404);});
		return md;
	}
	
	default boolean exists(CcpMapDecorator data) {
		boolean exists = this.getDao().exists(this, data);
		return exists;
	}
	
	CcpMapDecorator getOnlyExistingFields(CcpMapDecorator values) ;
	
	default CcpMapDecorator createOrUpdate(CcpMapDecorator values) {
		CcpMapDecorator onlyExistingFields = this.getOnlyExistingFields(values);
		CcpDao dao = this.getDao();

		boolean created = dao.createOrUpdate(this.name(), onlyExistingFields);
		
		this.saveAuditory(values, created ? CcpOperationType.create : CcpOperationType.update);

		return onlyExistingFields;
	}
	

	CcpDao getDao();
	
	default CcpMapDecorator delete(CcpMapDecorator values) {
		CcpDao crud = this.getDao();
		CcpMapDecorator remove = crud.delete(this, values);
		this.saveAuditory(values, CcpOperationType.delete);
		return remove;
	}
	
	default List<CcpMapDecorator> getManyByIds(CcpMapDecorator... values){
		String[] ids = new String[values.length];
		
		int k = 0;
		
		for (CcpMapDecorator value : values) {
			String id = this.getId(value);
			ids[k++] = id;
		}
		CcpDao crud = this.getDao();
		List<CcpMapDecorator> manyByIds = crud.getManyByIds(this, ids);
	
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
	void saveAuditory(CcpMapDecorator values, CcpOperationType operation);
}

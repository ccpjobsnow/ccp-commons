package com.ccp.especifications.db.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.db.dao.CcpDao;
import com.ccp.exceptions.db.CcpEntityMissingKeys;
import com.ccp.exceptions.db.CcpEntityRecordNotFound;
import com.ccp.exceptions.process.CcpFlow;


public interface CcpEntity extends CcpEntityIdGenerator{

	
	CcpTimeOption getTimeOption();
	
	CcpEntityField[] getFields();
	
	default String getId(CcpMapDecorator values) {
		CcpTimeOption timeOption = this.getTimeOption();
		Long time = System.currentTimeMillis();
		String formattedCurrentDate = timeOption.getFormattedCurrentDate(time);
		CcpEntityField[] fields = this.getFields();
		if(fields.length == 0) {
			
			if(CcpTimeOption.none != timeOption) {
				return formattedCurrentDate;
			}
			
			return UUID.randomUUID().toString();
		}
		
		List<CcpEntityField> missingKeys = Arrays.asList(fields).stream().filter(key -> this.isEmptyPrimaryKey(key, values))
				.collect(Collectors.toList());
		
		boolean isMissingKeys = missingKeys.isEmpty() == false;
		
		if(isMissingKeys) {
			throw new CcpEntityMissingKeys(this, missingKeys, values);
		}
		
		
		List<String> onlyPrimaryKeys = Arrays.asList(fields).stream().filter(key -> key.isPrimaryKey()).map(key -> this.getPrimaryKeyFieldValue(key, values)).collect(Collectors.toList());

		if(onlyPrimaryKeys.isEmpty()) {
			String hash = new CcpStringDecorator(formattedCurrentDate).hash().asString("SHA1");
			return hash;
		}
		
		Collections.sort(onlyPrimaryKeys);
		onlyPrimaryKeys = new ArrayList<>(onlyPrimaryKeys);

		if(formattedCurrentDate.trim().isEmpty() == false) {
			onlyPrimaryKeys.add(0, formattedCurrentDate);
		}
		
		String replace = onlyPrimaryKeys.toString().replace("[", "").replace("]", "");
		String hash = new CcpStringDecorator(replace).hash().asString("SHA1");
		return hash;
	}
	default boolean isEmptyPrimaryKey(CcpEntityField key, CcpMapDecorator values) {
		
		if(key.isPrimaryKey() == false) {
			return false;
		}
		
		String primaryKeyFieldValue = this.getPrimaryKeyFieldValue(key, values);
		if("_".equals(primaryKeyFieldValue)) {
			return true;
		}
		
		return false;
	}
	default String getPrimaryKeyFieldValue(CcpEntityField key, CcpMapDecorator values) {
		
		boolean notCollection = values.get(key.name()) instanceof Collection<?> == false;
		
		if(notCollection) {
			String primaryKeyFieldValue = values.getAsString(key.name()).trim() + "_";
			return primaryKeyFieldValue;
		}
		
		Collection<?> col = values.getAsObject(key.name());
		ArrayList<?> list = new ArrayList<>(col);
		list.sort((a, b) -> ("" + a).compareTo("" + b));
		String primaryKeyFieldValue = list.stream().map(x -> x.toString().trim()).collect(Collectors.toList()).toString() + "_";
		return primaryKeyFieldValue;
	}


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
		//TODO SALVAR AUDITORIA???
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
	
	default CcpMapDecorator transferData(CcpMapDecorator values, CcpEntity target) {
		this.delete(values);
		target.create(values);
		return values;
	}
	
	
}

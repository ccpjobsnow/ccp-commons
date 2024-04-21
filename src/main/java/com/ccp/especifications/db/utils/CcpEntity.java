package com.ccp.especifications.db.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.db.bulk.CcpEntityOperationType;
import com.ccp.especifications.db.dao.CcpDao;
import com.ccp.exceptions.db.CcpEntityMissingKeys;
import com.ccp.exceptions.db.CcpEntityRecordNotFound;
import com.ccp.exceptions.process.CcpFlow;


public interface CcpEntity extends CcpEntityIdGenerator{

	
	CcpTimeOption getTimeOption();
	
	CcpEntityField[] getFields();
	
	default String getId(CcpJsonRepresentation values) {
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
	
	default boolean isEmptyPrimaryKey(CcpEntityField key, CcpJsonRepresentation values) {
		
		if(key.isPrimaryKey() == false) {
			return false;
		}
		
		String primaryKeyFieldValue = this.getPrimaryKeyFieldValue(key, values);
		if("_".equals(primaryKeyFieldValue)) {
			return true;
		}
		
		return false;
	}
	default String getPrimaryKeyFieldValue(CcpEntityField key, CcpJsonRepresentation values) {
		
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


	default CcpJsonRepresentation getOneById(CcpJsonRepresentation data, Function<CcpJsonRepresentation, CcpJsonRepresentation> ifNotFound) {
		try {
			CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);
			CcpJsonRepresentation oneById = dao.getOneById(this, data);
			return oneById;
			
		} catch (CcpEntityRecordNotFound e) {
			CcpJsonRepresentation execute = ifNotFound.apply(data);
			return execute;
		}
	}

	default CcpJsonRepresentation getOneById(CcpJsonRepresentation data) {
		String entityName = this.getEntityName();
		CcpJsonRepresentation md = this.getOneById(data, x -> {throw new CcpFlow(x.put("entity", entityName), 404);});
		return md;
	}
	

	default CcpJsonRepresentation getOneById(String id) {
		try {
			CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);
			CcpJsonRepresentation md = dao.getOneById(this, id);
			return md;
			
		} catch (CcpEntityRecordNotFound e) {
			String entityName = this.getEntityName();
			CcpJsonRepresentation put = CcpConstants.EMPTY_JSON.put("id", id).put("entity", entityName);
			throw new CcpFlow(put, 404);
		}
	}
	
	default boolean exists(String id) {
		CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);
		boolean exists = dao.exists(this, id);
		return exists;
		
	}
	
	default boolean exists(CcpJsonRepresentation data) {
		CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);
		boolean exists = dao.exists(this, data);
		return exists;
	}
	
	CcpJsonRepresentation getOnlyExistingFields(CcpJsonRepresentation values) ;
	
	default CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation values) {
		CcpJsonRepresentation onlyExistingFields = this.getOnlyExistingFields(values);
		boolean created = this.create(values);
		
		this.saveAuditory(values, created ? CcpEntityOperationType.create : CcpEntityOperationType.update);

		return onlyExistingFields;
	}

	default boolean create(CcpJsonRepresentation values) {
		CcpJsonRepresentation onlyExistingFields = this.getOnlyExistingFields(values);
		CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);

		CcpJsonRepresentation createOrUpdate = dao.createOrUpdate(this, onlyExistingFields);
		String result = createOrUpdate.getAsString("result");
		boolean created = "created".equals(result);
		
		return created;
	}
	

	default boolean delete(CcpJsonRepresentation values) {
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
	
	default List<CcpJsonRepresentation> getManyByIds(String...ids){
		CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);
		List<CcpJsonRepresentation> manyByIds = dao.getManyByIds(this, ids);
	
		int k = 0;
		List<CcpJsonRepresentation> response = new ArrayList<>();
		for (String id : ids) {
			CcpJsonRepresentation md = manyByIds.get(k++);
			md = md.put("_id", id);
			response.add(md);
		}
		return response;
	}
	
	default List<CcpJsonRepresentation> getManyByIds(CcpJsonRepresentation... values){
		String[] ids = new String[values.length];
		
		int k = 0;
		
		for (CcpJsonRepresentation value : values) {
			String id = this.getId(value);
			ids[k++] = id;
		}
		List<CcpJsonRepresentation> response = getManyById(ids);
		return response;
	}

	default List<CcpJsonRepresentation> getManyById(List<String> ids){
		String[] array = ids.toArray(new String[ids.size()]);
		List<CcpJsonRepresentation> manyById = this.getManyById(array);
		return manyById;
	}
	
	default List<CcpJsonRepresentation> getManyById(String... ids) {
		int k;
		CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);
		List<CcpJsonRepresentation> manyByIds = dao.getManyByIds(this, ids);
	
		k = 0;
		List<CcpJsonRepresentation> response = new ArrayList<>();
		for (String id : ids) {
			CcpJsonRepresentation md = manyByIds.get(k++);
			md = md.put("_id", id);
			response.add(md);
		}
		return response;
	}
	
	default List<CcpJsonRepresentation> getManyByIds(List< CcpJsonRepresentation> values){
		CcpJsonRepresentation[] array = values.toArray(new CcpJsonRepresentation[values.size()]);
		return this.getManyByIds(array);
	}	
	void saveAuditory(CcpJsonRepresentation values, CcpEntityOperationType operation);
	
	boolean isAuditable();
	
	default CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation data, String id) {
		CcpDao dao = CcpDependencyInjection.getDependency(CcpDao.class);
		CcpJsonRepresentation createOrUpdate = dao.createOrUpdate(this, data, id);
		return createOrUpdate;
	}
	
	default CcpJsonRepresentation transferData(CcpJsonRepresentation values, CcpEntity target) {
		this.delete(values);
		target.create(values);
		return values;
	}
	
	
}

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
import com.ccp.especifications.db.crud.CcpCrud;
import com.ccp.exceptions.db.CcpEntityMissingKeys;
import com.ccp.exceptions.db.CcpEntityRecordNotFound;
import com.ccp.exceptions.process.CcpFlow;


public interface CcpEntity{


	String getEntityName();

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
			CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
			CcpJsonRepresentation oneById = crud.getOneById(this, data);
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
			CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
			CcpJsonRepresentation md = crud.getOneById(this, id);
			return md;
			
		} catch (CcpEntityRecordNotFound e) {
			String entityName = this.getEntityName();
			CcpJsonRepresentation put = CcpConstants.EMPTY_JSON.put("id", id).put("entity", entityName);
			throw new CcpFlow(put, 404);
		}
	}
	
	default boolean exists(String id) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		boolean exists = crud.exists(this, id);
		return exists;
		
	}
	
	default boolean exists(CcpJsonRepresentation data) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		boolean exists = crud.exists(this, data);
		return exists;
	}
	
	CcpJsonRepresentation getOnlyExistingFields(CcpJsonRepresentation values) ;
	
	default CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation values) {
		CcpJsonRepresentation onlyExistingFields = this.getOnlyExistingFields(values);
		boolean created = this.create(values);
		
		this.saveAuditory(onlyExistingFields, created ? CcpEntityOperationType.create : CcpEntityOperationType.update);

		return onlyExistingFields;
	}

	default boolean create(CcpJsonRepresentation values) {
		CcpJsonRepresentation onlyExistingFields = this.getOnlyExistingFields(values);
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);

		CcpJsonRepresentation createOrUpdate = crud.createOrUpdate(this, onlyExistingFields);
		String result = createOrUpdate.getAsString("result");
		boolean created = "created".equals(result);
		
		return created;
	}
	

	default boolean delete(CcpJsonRepresentation values) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		boolean remove = crud.delete(this, values);
		this.saveAuditory(values, CcpEntityOperationType.delete);
		return remove;
	}
	
	default boolean delete(String id) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		boolean remove = crud.delete(this, id);
		//TODO SALVAR AUDITORIA???
		return remove;
	}

	void saveAuditory(CcpJsonRepresentation values, CcpEntityOperationType operation);
	
	boolean isAuditable();
	
	default CcpJsonRepresentation createOrUpdate(CcpJsonRepresentation data, String id) {
		CcpCrud crud = CcpDependencyInjection.getDependency(CcpCrud.class);
		CcpJsonRepresentation createOrUpdate = crud.createOrUpdate(this, data, id);
		return createOrUpdate;
	}
	
	default CcpJsonRepresentation transferData(CcpJsonRepresentation values, CcpEntity target) {
		this.delete(values);
		target.create(values);
		return values;
	}
	
	
}

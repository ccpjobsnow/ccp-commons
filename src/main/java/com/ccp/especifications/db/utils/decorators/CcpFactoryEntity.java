package com.ccp.especifications.db.utils.decorators;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.db.bulk.CcpBulkItem;
import com.ccp.especifications.db.bulk.CcpEntityOperationType;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpEntityField;
import com.ccp.especifications.json.CcpJsonHandler;

public class CcpFactoryEntity {

	public static CcpEntity getEntityInstance(Class<?> configurationClass) {
		
		CcpEntity entity = getSimpleEntity(configurationClass);
		
		boolean isAuditableEntity = configurationClass.isAnnotationPresent(CcpEntityAuditable.class);
		if(isAuditableEntity) {
			CcpEntityAuditable annotation = configurationClass.getAnnotation(CcpEntityAuditable.class);
			Class<?>auditableEntityFactory = annotation.auditableEntityFactory();
			entity = getAuditEntity(auditableEntityFactory, entity);
		}		

		boolean isExpurgableEntity = configurationClass.isAnnotationPresent(CcpEntityExpurgable.class);
		
		if(isAuditableEntity && isExpurgableEntity) {
			throw new RuntimeException("The class '" + configurationClass.getName() + "' can not be annoted by '" 
		+ CcpEntityAuditable.class.getName() + "' annotation and '" + CcpEntityExpurgable.class.getName() + "' at the same time");

		}
		int cacheExpires = CcpLongevityEntity.daily.cacheExpires;
		
		if(isExpurgableEntity) {
			CcpEntityExpurgable annotation = configurationClass.getAnnotation(CcpEntityExpurgable.class);
			
			Class<?>expurgableEntityFactory = annotation.expurgableEntityFactory();
			CcpLongevityEntity longevity = annotation.longevityEntity();
			cacheExpires = longevity.cacheExpires;
			entity = getExpurgableEntity(expurgableEntityFactory, longevity, entity);
		}		

		CcpEntitySpecifications configuration = configurationClass.getAnnotation(CcpEntitySpecifications.class);
		boolean isCacheableEntity = configuration.cacheableEntity();
		
		if(isCacheableEntity) {
			entity = new CacheEntity(entity, cacheExpires);
		}
		
		boolean isReplicableEntity = configurationClass.isAnnotationPresent(CcpEntityTwin.class);
		
		if(isReplicableEntity) {
			CcpEntityTwin annotation = configurationClass.getAnnotation(CcpEntityTwin.class);
			String twinEntityName = annotation.twinEntityName();
			entity = new ReplicableEntity(twinEntityName, entity);
		}
		return entity;
	}

	private static CcpEntity getAuditEntity(Class<?> auditClass, CcpEntity entity) {
		try {
			Constructor<?> declaredConstructor = auditClass.getDeclaredConstructor(CcpEntity.class);
			declaredConstructor.setAccessible(true);
			CcpEntity newInstance = (CcpEntity) declaredConstructor.newInstance(entity);
			return newInstance;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static CcpEntity getExpurgableEntity(Class<?> auditClass, CcpLongevityEntity longevity, CcpEntity entity) {
		try {
			Constructor<?> declaredConstructor = auditClass.getDeclaredConstructor(CcpEntity.class, CcpLongevityEntity.class);
			declaredConstructor.setAccessible(true);
			CcpEntity newInstance = (CcpEntity) declaredConstructor.newInstance(entity, longevity);
			return newInstance;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static CcpEntity getSimpleEntity(Class<?> configurationClass) {
		boolean isNotAnotted = configurationClass.isAnnotationPresent(CcpEntitySpecifications.class) == false;

		if(isNotAnotted) {
			throw new RuntimeException("The class '" + configurationClass.getName() + "' must be annoted with '" + CcpEntitySpecifications.class.getName() + "' annotation");
		}

		CcpEntitySpecifications annotation = configurationClass.getAnnotation(CcpEntitySpecifications.class);
		
		CcpEntityField[] entityFields = getFields(configurationClass);
		boolean virtualEntity = annotation.virtualEntity();
		
		CcpEntity entity = new BaseEntity(configurationClass, virtualEntity, entityFields);
		return entity;
	}

	public static List<CcpBulkItem> getFirstRecordsToInsert(Class<?> configurationClass) {
		
		boolean isNotAnotted = configurationClass.isAnnotationPresent(CcpEntitySpecifications.class) == false;

		if(isNotAnotted) {
			throw new RuntimeException("The class '" + configurationClass.getName() + "' must be annoted with '" + CcpEntitySpecifications.class.getName() + "' annotation");
		}

		CcpEntitySpecifications annotation = configurationClass.getAnnotation(CcpEntitySpecifications.class);
		CcpEntity entityInstance = getEntityInstance(configurationClass);
		String pathToFirstRecords = annotation.pathToFirstRecords();
		
		boolean hasNoFirstRecordsToInsert = pathToFirstRecords.trim().isEmpty();
		
		if(hasNoFirstRecordsToInsert) {
			return new ArrayList<>();
		}
		
		String stringContent = new CcpStringDecorator(pathToFirstRecords).file().getStringContent();
		List<Map<String, Object>> list =  CcpDependencyInjection.getDependency(CcpJsonHandler.class).fromJson(stringContent);
		List<CcpJsonRepresentation> jsons = list.stream().map(x -> new CcpJsonRepresentation(x)).collect(Collectors.toList());
		
		List<CcpBulkItem> bulkItems = jsons.stream().map(json -> new CcpBulkItem(json, CcpEntityOperationType.create, entityInstance)).collect(Collectors.toList());
		
		return bulkItems;
	}

	private static CcpEntityField[] getFields(Class<?> configurationClass) {
		String className = configurationClass.getName();

		Class<?>[] declaredClasses = configurationClass.getDeclaredClasses();
		
		boolean didNotDeclareFieldsEnum = declaredClasses.length == 0;
		if(didNotDeclareFieldsEnum) {
			throw new RuntimeException("The class '" + className + "' must declare a public static enum");
		}
		
		Class<?> firstClass = declaredClasses[0];
		
		boolean incorrectClassName = firstClass.getSimpleName().equals("FIELDS") == false;
		
		if(incorrectClassName) {
			throw new RuntimeException("The class '" + className + "' must declare a public static enum called 'FIELDS'");
		}
		
		Method method;
		try {
			method = firstClass.getMethod("values");
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("The class '" + className + "' must declare a public static enum", e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		}
		CcpEntityField[] fields;
		try {
			fields = (CcpEntityField[])method.invoke(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		boolean hasNoFields = fields.length == 0;

		if(hasNoFields) {
			throw new RuntimeException("The class '" + className + "' must have an enum with 1 item at least");
		}
		
		CcpEntityField field = fields[0];
		
		boolean doesNotImplementTheInterface = field instanceof CcpEntityField == false;
		
		if(doesNotImplementTheInterface) {
			throw new RuntimeException("The class '" + className + "' must have an enum that implements the interface " + CcpEntityField.class.getName());
		}
		
		return fields;
	}
	
	
}

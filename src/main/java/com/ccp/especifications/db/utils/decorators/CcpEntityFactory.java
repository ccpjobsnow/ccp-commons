package com.ccp.especifications.db.utils.decorators;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.ccp.decorators.CcpStringDecorator;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpEntityField;

public class CcpEntityFactory {

	public final CcpEntityField[] entityFields;

	public final CcpEntity entityInstance;

	public final boolean hasTwinEntity;
	
	public CcpEntityFactory(Class<?> configurationClass) {
		
		this.hasTwinEntity = configurationClass.isAnnotationPresent(CcpEntityTwin.class);
		this.entityFields = this.getFields(configurationClass);
		this.entityInstance = this.getEntityInstance(configurationClass);
	}
	
	private CcpEntity getEntityInstance(Class<?> configurationClass) {
		String simpleName = configurationClass.getSimpleName();
		String snackCase = new CcpStringDecorator(simpleName).text().toSnakeCase().content;
		String substring = snackCase.substring(snackCase.indexOf("entity") + 7);
	
		CcpEntity entity = new BaseEntity(substring,  this.entityFields);
		
		boolean isAuditableEntity = configurationClass.isAnnotationPresent(CcpEntityVersionable.class);
		if(isAuditableEntity) {
			CcpEntityVersionable annotation = configurationClass.getAnnotation(CcpEntityVersionable.class);
			Class<?>auditableEntityFactory = annotation.versionableEntityFactory();
			entity = getAuditEntity(auditableEntityFactory, entity);
		}		

		boolean isExpurgableEntity = configurationClass.isAnnotationPresent(CcpEntityExpurgable.class);
		
		if(isAuditableEntity && isExpurgableEntity) {
			throw new RuntimeException("The class '" + configurationClass.getName() + "' can not be annoted by '" 
		+ CcpEntityVersionable.class.getName() + "' annotation and '" + CcpEntityExpurgable.class.getName() + "' at the same time");

		}
		int cacheExpires = CcpEntityExpurgableOptions.daily.cacheExpires;
		
		if(isExpurgableEntity) {
			CcpEntityExpurgable annotation = configurationClass.getAnnotation(CcpEntityExpurgable.class);
			
			Class<?>expurgableEntityFactory = annotation.expurgableEntityFactory();
			CcpEntityExpurgableOptions longevity = annotation.expurgTime();
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

	private static CcpEntity getExpurgableEntity(Class<?> auditClass, CcpEntityExpurgableOptions longevity, CcpEntity entity) {
		try {
			Constructor<?> declaredConstructor = auditClass.getDeclaredConstructor(CcpEntity.class, CcpEntityExpurgableOptions.class);
			declaredConstructor.setAccessible(true);
			CcpEntity newInstance = (CcpEntity) declaredConstructor.newInstance(entity, longevity);
			return newInstance;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private CcpEntityField[] getFields(Class<?> configurationClass) {
		String className = configurationClass.getName();

		Class<?>[] declaredClasses = configurationClass.getDeclaredClasses();
		
		boolean didNotDeclareFieldsEnum = declaredClasses.length == 0;
		if(didNotDeclareFieldsEnum) {
			throw new RuntimeException("The class '" + className + "' must declare a public static enum");
		}
		
		Class<?> firstClass = declaredClasses[0];
		
		boolean incorrectClassName = firstClass.getSimpleName().equals("Fields") == false;
		
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

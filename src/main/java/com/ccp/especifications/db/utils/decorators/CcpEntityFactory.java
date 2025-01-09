package com.ccp.especifications.db.utils.decorators;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpEntityField;

public class CcpEntityFactory {

	public final CcpEntityField[] entityFields;

	public final Class<?> configurationClass;

	public final CcpEntity entityInstance;

	public final boolean hasTwinEntity;
	
	
	public CcpEntityFactory(Class<?> configurationClass) {
		
		this.hasTwinEntity = configurationClass.isAnnotationPresent(CcpEntityTwin.class);
		this.entityFields = this.getFields(configurationClass);
		this.entityInstance = this.getTwinEntity(configurationClass);
		this.configurationClass = configurationClass;
	}
	
	private CcpEntity getTwinEntity(Class<?> configurationClass) {
		boolean isNotTwinEntity = configurationClass.isAnnotationPresent(CcpEntityTwin.class) == false;
		
		if(isNotTwinEntity) {
			CcpEntity entity = this.getEntityInstance(configurationClass);
			return entity;
		}
		CcpEntityTwin annotation = configurationClass.getAnnotation(CcpEntityTwin.class);
		String twinEntityName = annotation.twinEntityName();
		
		CcpEntity original = this.getEntityInstance(configurationClass);
		CcpEntity twin = this.getEntityInstance(configurationClass, twinEntityName);
		
		DecoratorTwinEntity entity = new DecoratorTwinEntity(original, twin);
		return entity;
	}
	
	private CcpEntity getEntityInstance(Class<?> configurationClass) {
		
		String simpleName = configurationClass.getSimpleName();
		String snackCase = new CcpStringDecorator(simpleName).text().toSnakeCase().content;
		int indexOf = snackCase.indexOf("entity");
		String entityName = snackCase.substring(indexOf + 7);
	
		CcpEntity entity = this.getEntityInstance(configurationClass, entityName);
		return entity;
	}

	private CcpEntity getEntityInstance(Class<?> configurationClass, String entityName) {
		CcpEntitySpecifications ann = configurationClass.getAnnotation(CcpEntitySpecifications.class);
		Class<?>[] jsonTransformationsArray = ann.jsonTransformations();
		 List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> jsonTransformationsList = Arrays.asList(jsonTransformationsArray).stream()
				.map(x -> intanciateFunction(x))
				.collect(Collectors.toList());
		CcpEntity entity = new DefaultImplementationEntity(entityName, configurationClass, jsonTransformationsList,  this.entityFields);
		
		boolean hasDecorators = configurationClass.isAnnotationPresent(CcpEntityDecorators.class);
	
		if(hasDecorators) {
			CcpEntityDecorators annotation = configurationClass.getAnnotation(CcpEntityDecorators.class);
			Class<?>[] decorators = annotation.decorators();
			entity = getDecoratedEntity(entity, decorators);
		}		

		boolean isExpurgableEntity = configurationClass.isAnnotationPresent(CcpEntityExpurgable.class);
		
		if(hasDecorators && isExpurgableEntity) {
			throw new RuntimeException("The class '" + configurationClass.getName() + "' can not be annoted by '" 
		+ CcpEntityDecorators.class.getName() + "' annotation and '" + CcpEntityExpurgable.class.getName() + "' at the same time");

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
			entity = new DecoratorCacheEntity(entity, cacheExpires);
		}
		
		return entity;
	}

	@SuppressWarnings("unchecked")
	private Function<CcpJsonRepresentation, CcpJsonRepresentation> intanciateFunction(Class<?> x) {
		try {
			Constructor<?> declaredConstructor = x.getDeclaredConstructor();
			declaredConstructor.setAccessible(true);
			Object newInstance = declaredConstructor.newInstance();
			return (Function<CcpJsonRepresentation, CcpJsonRepresentation>) newInstance;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static CcpEntity getDecoratedEntity(CcpEntity entity, Class<?>... decorators) {
		try {
			for (Class<?> decorator : decorators) {
				Constructor<?> declaredConstructor = decorator.getDeclaredConstructor();
				declaredConstructor.setAccessible(true);
				CcpEntityDecoratorFactory newInstance = (CcpEntityDecoratorFactory) declaredConstructor.newInstance();
				entity = newInstance.getEntity(entity);
			}
			return entity;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static CcpEntity getExpurgableEntity(Class<?> decorator, CcpEntityExpurgableOptions longevity, CcpEntity entity) {
		try {
			Constructor<?> declaredConstructor = decorator.getDeclaredConstructor();
			declaredConstructor.setAccessible(true);
			CcpEntityExpurgableFactory newInstance = (CcpEntityExpurgableFactory) declaredConstructor.newInstance();
			CcpEntity expurgableEntity = newInstance.getEntity(entity, longevity);
			return expurgableEntity;
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

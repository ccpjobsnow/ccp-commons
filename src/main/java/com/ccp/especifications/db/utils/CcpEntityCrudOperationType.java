package com.ccp.especifications.db.utils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.decorators.configurations.CcpEntityOperationSpecification;
import com.ccp.especifications.db.utils.decorators.configurations.CcpEntitySpecifications;
import com.ccp.validation.CcpJsonFieldsValidations;

public enum CcpEntityCrudOperationType 
//implements CcpHandleWithSearchResultsInTheEntity<List<CcpBulkItem>>
{
	save {
		public CcpJsonRepresentation execute(CcpEntity entity, CcpJsonRepresentation json) {
			entity.createOrUpdate(json);
			return json;
		}

		public List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> getStepsBefore(Class<?> entityClass) {
			CcpEntitySpecifications especifications = super.getEspecifications(entityClass);
			CcpEntityOperationSpecification operation = especifications.save();
			Class<?>[] callBacks = operation.beforeOperation();
			List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> collect = Arrays.asList(callBacks).stream().map(x -> instanciateFunction(x)).collect(Collectors.toList());
			return  collect;
		}

		public List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> getStepsAfter(Class<?> entityClass) {
			CcpEntitySpecifications especifications = super.getEspecifications(entityClass);
			CcpEntityOperationSpecification operation = especifications.save();
			Class<?>[] callBacks = operation.afterOperation();
			List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> collect = Arrays.asList(callBacks).stream().map(x -> instanciateFunction(x)).collect(Collectors.toList());
			return  collect;
		}

		public void validate(Class<?> entityClass, CcpEntityCrudOperationType operation, CcpJsonRepresentation json) {
			CcpEntitySpecifications especifications = super.getEspecifications(entityClass);
			CcpEntityOperationSpecification op = especifications.save();
			Class<?> jsonValidationClass = op.classWithFieldsValidationsRules();
			String featureName = entityClass.getName()+ "." + this;
			CcpJsonFieldsValidations.validate(jsonValidationClass, json.content, featureName);
		}

		
	},
	delete {

		public CcpJsonRepresentation execute(CcpEntity entity, CcpJsonRepresentation json) {
			entity.delete(json);
			return json;
		}
		public List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> getStepsBefore(Class<?> entityClass) {
			CcpEntitySpecifications especifications = super.getEspecifications(entityClass);
			CcpEntityOperationSpecification operation = especifications.delete();
			Class<?>[] callBacks = operation.beforeOperation();
			List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> collect = Arrays.asList(callBacks).stream().map(x -> instanciateFunction(x)).collect(Collectors.toList());
			return  collect;
		}

		public List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> getStepsAfter(Class<?> entityClass) {
			CcpEntitySpecifications especifications = super.getEspecifications(entityClass);
			CcpEntityOperationSpecification operation = especifications.delete();
			Class<?>[] callBacks = operation.afterOperation();
			List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> collect = Arrays.asList(callBacks).stream().map(x -> instanciateFunction(x)).collect(Collectors.toList());
			return  collect;
		}

		public void validate(Class<?> entityClass, CcpEntityCrudOperationType operation, CcpJsonRepresentation json) {
			CcpEntitySpecifications especifications = super.getEspecifications(entityClass);
			CcpEntityOperationSpecification op = especifications.delete();
			Class<?> jsonValidationClass = op.classWithFieldsValidationsRules();
			String featureName = entityClass.getName()+ "." + this;
			CcpJsonFieldsValidations.validate(jsonValidationClass, json.content, featureName);
		}


		
	}
	,
	
	none {

		public CcpJsonRepresentation execute(CcpEntity entity, CcpJsonRepresentation json) {
			return json;
		}

		public List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> getStepsBefore(Class<?> entityClass) {
			return new ArrayList<>();
		}

		public List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> getStepsAfter(Class<?> entityClass) {
			return new ArrayList<>();
		}

		public void validate(Class<?> entityClass, CcpEntityCrudOperationType operation, CcpJsonRepresentation json) {

		}
	}
	
	;

	public abstract CcpJsonRepresentation execute(CcpEntity entity, CcpJsonRepresentation json);

	public abstract List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> getStepsBefore(
			Class<?> entityClass);

	public abstract List<Function<CcpJsonRepresentation, CcpJsonRepresentation>> getStepsAfter(Class<?> entityClass);

	public abstract void validate(Class<?> entityClass, CcpEntityCrudOperationType operation, CcpJsonRepresentation json);
	
	@SuppressWarnings("unchecked")
	public static Function<CcpJsonRepresentation, CcpJsonRepresentation> instanciateFunction(Class<?> x) {
		try {
			Constructor<?> declaredConstructor = x.getDeclaredConstructor();
			declaredConstructor.setAccessible(true);
			Object newInstance = declaredConstructor.newInstance();
			return (Function<CcpJsonRepresentation, CcpJsonRepresentation>) newInstance;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private CcpEntitySpecifications getEspecifications(Class<?> entityClass) {
		if(entityClass.isAnnotationPresent(CcpEntitySpecifications.class) == false) {
			throw new RuntimeException("The class '" + entityClass + "' is not annoted by " + CcpEntitySpecifications.class.getName());
		}
		CcpEntitySpecifications annotation = entityClass.getAnnotation(CcpEntitySpecifications.class);
		return annotation;
	}
	


}

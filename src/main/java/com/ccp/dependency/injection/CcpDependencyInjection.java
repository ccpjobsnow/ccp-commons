package com.ccp.dependency.injection;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CcpDependencyInjection {

	static Map<Class<?>, Object> instances = new HashMap<>();
	
	public static void loadAllInstances(CcpModuleExporter... providers) {
		
		for (CcpModuleExporter provider : providers) {
			Object implementation = provider.export();
			Class<? extends Object> class1 = implementation.getClass();
			Class<?>[] interfaces = class1.getInterfaces();
			Class<?> especification = interfaces[0];
			instances.put(especification, implementation);
		}
		
		Collection<Object> values = instances.values();

		for (Object instance : values) {
			injectDependencies(instance);
		}
	}
	

	private static void injectDependencies(Object instance) {
		Field[] declaredFields = instance.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			injectDependencies(field, instance);
		}
	}
	
	private static void injectDependencies(Field field, Object instance) {
		
		boolean annotationPresent = field.isAnnotationPresent(CcpSpecification.class);
		
		if(annotationPresent == false) {
			return;
		}
		try {
			Object object = field.get(instance);
			boolean alreadyInjected = object != null;
			
			if(alreadyInjected) {
				return;
			}
			Class<?> especificationClass = field.getType();
			Object implementation = instances.get(especificationClass);
			injectDependencies(implementation);
			field.setAccessible(true);
			field.set(instance, implementation);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
}

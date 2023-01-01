package com.ccp.dependency.injection;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CcpDependencyInjection {

	static Map<Class<?>, Object> instances = new HashMap<>();
	
	public static void loadAllImplementationsProviders(CcpModuleExporter... providers) {
		
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
	
	public static void injectDependencies(Object... instances) {
		for (Object instance : instances) {
			injectDependencies(instance);
		}
	}

	private static void injectDependencies(Object instance) {
		Field[] declaredFields = instance.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			injectDependencies(field, instance);
		}
	}
	static Set<String> fields = new HashSet<>();
	
	private static void injectDependencies(Field field, Object instance) {
		
		boolean annotationPresent = field.isAnnotationPresent(CcpDependencyInject.class);
		
		if(annotationPresent == false) {
			return;
		}
		
		try {
			String fieldKey = instance.getClass().getName() + "." + instance.hashCode() + "." + field.getName();
			
			boolean esteFieldJaFoiRegistrado = fields.contains(fieldKey);
			
			if(esteFieldJaFoiRegistrado) {
				return;
			}
			
			field.setAccessible(true);
			
			Object object = field.get(instance);
			
			if(object != null) {
				injectDependencies(object);
				fields.add(fieldKey);
				return;
			}
			
			Class<?> especificationClass = field.getType();
			Object implementation = instances.get(especificationClass);
			
			if(implementation == null) {
				throw new RuntimeException("It is missing an implementation of the class " + especificationClass.getName());
			}
			
			injectDependencies(implementation);
			field.set(instance, implementation);
			fields.add(fieldKey);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
}

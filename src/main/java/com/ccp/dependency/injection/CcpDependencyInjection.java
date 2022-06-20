package com.ccp.dependency.injection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.ccp.dependency.injection.CcpEspecification.DefaultImplementationProvider;

public class CcpDependencyInjection {

	static Map<Class<?>, Map<Class<? extends CcpImplementationProvider>, Object>> instances = new HashMap<>();
	
	
	@SuppressWarnings("unchecked")
	public static void loadImplementations(CcpImplementationProvider...providers) {
		for (CcpImplementationProvider provider : providers) {
			Object implementation = provider.getImplementation();
			Class<? extends CcpImplementationProvider> implementationClass =(Class<? extends CcpImplementationProvider>) implementation.getClass();
			Class<?>[] interfaces = implementationClass.getInterfaces();
			if(interfaces.length != 1) {
				throw new RuntimeException("Invalid implementation in the class " + implementationClass.getName());
			}
			
			Class<?> especification = interfaces[0];
			Map<Class<? extends CcpImplementationProvider>, Object> implementations = instances.getOrDefault(especification, new HashMap<>());
			implementations.put(implementationClass, implementation);
			instances.put(especification, implementations);
		}
	}

	public static void injectDependencies(Object instance) {
		Field[] declaredFields = instance.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			injectDependencies(field, instance);
		}
	}
	
	private static void injectDependencies(Field field, Object instance) {
		
		boolean annotationPresent = field.isAnnotationPresent(CcpEspecification.class);
		
		if(annotationPresent == false) {
			return;
		}
		try {
			Object object = field.get(instance);
			boolean alreadyInjected = object != null;
			
			if(alreadyInjected) {
				return;
			}
			injectDependencies(instance);
			Class<?> especificationClass = field.getType();
			CcpEspecification annotation = field.getAnnotation(CcpEspecification.class);
			Class<? extends CcpImplementationProvider> implementationProviderClass = annotation.value();
			Object implementation = getImplementation(especificationClass, implementationProviderClass);
			field.setAccessible(true);
			field.set(instance, implementation);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public static Object getImplementation(Class<?> especificationClass, Class<? extends CcpImplementationProvider> implementationProviderClass) {
		Map<Class<? extends CcpImplementationProvider>, Object> implementations = instances.get(especificationClass);
		
		if(implementations.isEmpty()) {
			throw new RuntimeException("Implementation mapping not found to " + especificationClass.getName());
		}
		
		boolean isDefaultImplementation = implementationProviderClass.equals(DefaultImplementationProvider.class);
		if(isDefaultImplementation) {
			Collection<Object> values = implementations.values();
			Object implementation = new ArrayList<>(values).get(0);
			return implementation;
		}

		Object implementation = implementations.get(implementationProviderClass);
		if(implementation == null) {
			throw new RuntimeException("Implementation mapping not found to " + especificationClass.getName() + " and " + implementationProviderClass.getName());
		}
		
		return implementation;
	}
}

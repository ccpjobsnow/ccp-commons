package com.ccp.dependency.injection;

import java.util.HashMap;
import java.util.Map;

public class CcpDependencyInjection {

	static Map<Class<?>, Object> instances = new HashMap<>();
	
	public static void loadAllDependencies(CcpInstanceProvider<?>... providers) {
		
		for (CcpInstanceProvider<?> provider : providers) {
			Object implementation = provider.getInstance();
			Class<? extends Object> class1 = implementation.getClass();
			Class<?>[] interfaces = class1.getInterfaces();
			Class<?> especification = interfaces[0];
			instances.put(especification, implementation);
		}
	}
	
	public static <T>boolean hasDependency(Class<T> interfaceClass) {
		Object implementation = instances.get(interfaceClass);
		return implementation != null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getDependency(Class<T> interfaceClass) {
		Object implementation = instances.get(interfaceClass);
		if(implementation == null) {
			throw new RuntimeException("It is missing an implementation of the class " + interfaceClass.getName());
		}
		return (T) implementation;
	}
	
	public static <T> T getInstance(Class<CcpInstanceProvider<T>> interfaceClass) {
		try {
			CcpInstanceProvider<T> newInstance = interfaceClass.getDeclaredConstructor().newInstance();
			T instance = newInstance.getInstance();
			return instance;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	
}

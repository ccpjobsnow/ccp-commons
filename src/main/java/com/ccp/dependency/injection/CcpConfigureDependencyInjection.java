package com.ccp.dependency.injection;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CcpConfigureDependencyInjection {

	public static void injectAllDependencies(Class<?> clazz) {
		
		if(clazz.isAnnotationPresent(CcpDependencyInjectionDirectives.class) == false) {
			throw new RuntimeException("A classe " + clazz.getName() + " não está anotada com  " + CcpDependencyInjectionDirectives.class.getName());
		}
		CcpDependencyInjectionDirectives annotation = clazz.getAnnotation(CcpDependencyInjectionDirectives.class);
		Class<?> businessPackage = annotation.businessPackage();
		Class<?>[] implementationPackages = annotation.implementationPackages();
		injectDependencies(businessPackage, implementationPackages);
		
	}
	
	private static  void injectDependencies(Class<?> businessPackage, Class<?>... implementationPackages) {
		List<Object> businessInstances = findAllClassesUsingClassLoader(businessPackage.getPackage().getName()).stream().map(x -> instanciate(x)).collect(Collectors.toList());
		for (Class<?> implementationPackage : implementationPackages) {
			Map<Class<?>, Object> injectDependencies = injectDependencies(implementationPackage);
			for (Object businessInstance : businessInstances) {
				injectDependencies(businessInstance, injectDependencies);
			}
		}
	}
	
	private static  Map<Class<?>, Object> injectDependencies(Class<?> implementationPackage) {
		
		Map<Class<?>, Object> dependencies = getDependencies(implementationPackage.getPackage().getName());
		Map<Class<?>, Object> newDependencies = new HashMap<>();
		dependencies.keySet().forEach(clazz -> {
			Object instance = dependencies.get(clazz);
			Object implementation = injectDependencies(instance, dependencies);
			newDependencies.put(clazz, implementation);
		});
		return newDependencies;
	}

	private static  Object injectDependencies (Object instance, Map<Class<?>, Object> dependencies) {
		Class<? extends Object> clazz = instance.getClass();
		
		if(isValidClass(clazz) == false) {
			return instance;
		}
		
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if(field.isAnnotationPresent(CcpEspecification.class) == false) {
				continue;
			}
			Class<?> especification = field.getType();
			Object implementation = dependencies.get(especification);
			if(implementation == null) {
				throw new RuntimeException("Não encontramos implementação válida para o field chamado " + field.getName() + " da classe " + clazz.getName());
			}
			implementation = injectDependencies(implementation, dependencies);
			field.setAccessible(true);
			try {
				Object value = field.get(instance);
				boolean alreadyInjected = value != null;
				if(alreadyInjected) {
					return instance;
				}
				field.set(implementation, instance);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return instance;
		
	}	
	
	private static  Object instanciate(Class<?> x) {
		try {
			Constructor<?> declaredConstructor = x.getDeclaredConstructor();
			declaredConstructor.setAccessible(true);
			return declaredConstructor.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	private static  Map<Class<?>, Object> getDependencies(String implementationPackage) {
		Map<Class<?>, Object> map = new HashMap<>();
		Set<Class<?>> findAllClassesUsingClassLoader = findAllClassesUsingClassLoader(implementationPackage);
		for (Class<?> class1 : findAllClassesUsingClassLoader) {
			Class<?>[] interfaces = class1.getInterfaces();
			String className = class1.getName();
			if (interfaces.length != 1) {
				throw new RuntimeException("A classe " + className + " possui " + interfaces.length
						+ " interfaces e deve possuir uma única");
			}
			Class<?> _interface = interfaces[0];
			try {
				Constructor<?> declaredConstructor = class1.getDeclaredConstructor();
				declaredConstructor.setAccessible(true);
				Object newInstance = declaredConstructor.newInstance();
				map.put(_interface, newInstance);
			} catch (Exception e) {
				throw new RuntimeException("Problema com a classe " + className, e);
			}
		}
		return map;
	}

	private static  Set<Class<?>> findAllClassesUsingClassLoader(String packageName) {
		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("[.]", "/"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		return reader.lines().filter(line -> line.endsWith(".class"))
				.filter(line -> isValidClass(line, packageName))
				.map(line -> getClass(line, packageName))
				.collect(Collectors.toSet());
	}

	private static  Class<?> getClass(String className, String packageName) {
		try {
			Class<?> clazz = Class.forName(packageName + "." + className.substring(0, className.lastIndexOf('.')));
			return clazz;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}


	private static  boolean isValidClass(String className, String packageName) {
		Class<?> clazz;
		try {
			String fullClassName = className.substring(0, className.lastIndexOf('.'));
			clazz = Class.forName(packageName + "." + fullClassName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}

		boolean validClass = isValidClass(clazz);
		
		return validClass;
	}


	private static  boolean isValidClass(Class<?> clazz) {
		if(clazz.isAnnotationPresent(CcpBusiness.class)){
			return true;
		}
		if(clazz.isAnnotationPresent(CcpImplementation.class)){
			return true;
		}
		return false;
	}
}

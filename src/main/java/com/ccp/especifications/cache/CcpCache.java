package com.ccp.especifications.cache;

import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;

public interface CcpCache {

	 Object get(String key) ;
	
	@SuppressWarnings("unchecked")
	default <V> V get(String key, CcpJsonRepresentation json, Function<CcpJsonRepresentation, V> taskToGetValue, int cacheSeconds) {

		Object object = this.get(key);

		if (object != null) {
			return (V) object;
		}
		V value = taskToGetValue.apply(json);
		this.put(key, value, cacheSeconds);

		return value;
	}
	
	
	<V> V getOrDefault(String key, V defaultValue);
	<V> V getOrThrowException(String key, RuntimeException e);
	boolean isPresent(String key);
	void put(String key, Object value, int secondsDelay);
	<V> V delete(String key);
}

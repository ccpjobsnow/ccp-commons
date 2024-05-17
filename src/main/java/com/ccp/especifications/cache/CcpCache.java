package com.ccp.especifications.cache;

import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;

public interface CcpCache {
	<V> V get(String cacheKey,CcpJsonRepresentation cacheParameters, Function<CcpJsonRepresentation, V> cacheLayer, int cacheInTimeSeconds);
	<V> V getOrDefault(String key, V defaultValue);
	<V> V getOrThrowException(String key, RuntimeException e);
	boolean isPresent(String key);
	void put(String key, Object value, int secondsDelay);
	<V> V delete(String key);
}

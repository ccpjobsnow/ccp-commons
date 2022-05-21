package com.ccp.especifications.cache;

public interface CcpCache {
	<V> V get(String key, CcpTaskCache<V> taskToGetValue, int cacheSeconds);
	<V> V getOrDefault(String key, V defaultValue);
	<V> V getOrThrowException(String key, RuntimeException e);
	boolean isPresent(String key);
	void put(String key, Object value, int secondsDelay);
	<V> V remove(String key);
}

package com.ccp.especifications.cache;

public interface CcpCache {
	<V> V get(CcpTaskCache<V> taskToGetValue, int cacheSeconds);
	<V> V getOrDefault(V defaultValue);
	<V> V getOrThrowException(RuntimeException e);
	CcpCache incrementKey(String key, Object value);
	boolean isPresent();
	void put(Object value, int secondsDelay);
	<V> V remove();
}

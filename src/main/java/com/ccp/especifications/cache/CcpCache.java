package com.ccp.especifications.cache;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpMapTransform;

public interface CcpCache {
	<V> V get(String cacheKey,CcpMapDecorator cacheParameters, CcpMapTransform<V> cacheLayer, int cacheInTimeSeconds);
	<V> V getOrDefault(String key, V defaultValue);
	<V> V getOrThrowException(String key, RuntimeException e);
	boolean isPresent(String key);
	void put(String key, Object value, int secondsDelay);
	<V> V remove(String key);
}

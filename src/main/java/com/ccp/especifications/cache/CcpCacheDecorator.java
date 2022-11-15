package com.ccp.especifications.cache;

import com.ccp.constantes.CcpConstants;
import com.ccp.process.CcpMapTransform;

public class CcpCacheDecorator {
	private final CcpCache cache;
	
	private final String key;

	public CcpCacheDecorator(CcpCache cache, String key) {
		this.cache = cache;
		this.key = key;
	}

	public <V> V get(CcpMapTransform<V> taskToGetValue, int cacheSeconds) {
		return this.cache.get(this.key, CcpConstants.emptyJson, taskToGetValue, cacheSeconds);
	}

	public <V> V getOrDefault(V defaultValue) {
		return this.cache.getOrDefault(this.key, defaultValue);
	}

	public <V> V getOrThrowException(RuntimeException e) {
		return this.cache.getOrThrowException(this.key, e);
	}

	public boolean isPresent() {
		return this.cache.isPresent(this.key);
	}

	public void put(Object value, int secondsDelay) {
		this.cache.put(this.key, value, secondsDelay);
	}

	public <V> V remove() {
		return this.cache.remove(this.key);
	}
	
	public CcpCacheDecorator incrementKey(String key, Object value) {
		String _key = this.key + "." + key + "." + value;
		CcpCacheDecorator ccpCacheDecorator = new CcpCacheDecorator(cache, _key);
		return ccpCacheDecorator;
	}
}

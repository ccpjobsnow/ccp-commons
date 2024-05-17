package com.ccp.especifications.cache;

import java.util.Set;
import java.util.function.Function;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.dependency.injection.CcpDependencyInjection;

public class CcpCacheDecorator {
	private final CcpCache cache = CcpDependencyInjection.getDependency(CcpCache.class);
	
	private final CcpJsonRepresentation values;

	private final String key;
	

	public CcpCacheDecorator(String key) {
		this.values = CcpConstants.EMPTY_JSON;
		this.key = key;
	}
	
	private CcpCacheDecorator(CcpJsonRepresentation values, String key) {
		this.values = values;
		this.key = key;
	}

	public <V> V get(Function<CcpJsonRepresentation,V> taskToGetValue, int cacheSeconds) {
		return this.cache.get(this.key, this.values, taskToGetValue, cacheSeconds);
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
		CcpJsonRepresentation put = this.values.put(key, value);
		CcpCacheDecorator ccpCacheDecorator = new CcpCacheDecorator(put, _key);
		return ccpCacheDecorator;
	}
	
	public CcpCacheDecorator incrementKeys(CcpJsonRepresentation values, String... keys) {
		
		CcpJsonRepresentation jsonPiece = values.getJsonPiece(keys);
		
		CcpCacheDecorator result = this.incrementKeys(jsonPiece);
		
		return result;
	}

	public CcpCacheDecorator incrementKeys(CcpJsonRepresentation jsonPiece) {
		CcpCacheDecorator result = this;
		
		Set<String> keySet = jsonPiece.keySet();
		
		for (String key : keySet) {
			Object value = jsonPiece.get(key);
			result = result.incrementKey(key, value);
		}
		return result;
	}
}

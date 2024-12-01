package com.ccp.especifications.db.utils.decorators;

public enum CcpLongevityCache {
	DAY(86400),
	HOUR(3600),
	MINUTE(60),
	SECOND(1)
	;
	
	
	private CcpLongevityCache(int longevity) {
		this.cacheLongevity = longevity;
	}

	public final int cacheLongevity;
}

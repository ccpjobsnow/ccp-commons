package com.ccp.especifications.cache;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpTaskCache<V> {
	V getValue(CcpMapDecorator values);

}

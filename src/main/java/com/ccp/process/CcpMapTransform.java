package com.ccp.process;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpMapTransform<V> {
	V transform(CcpMapDecorator values);

}

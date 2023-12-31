package com.ccp.process;

import com.ccp.decorators.CcpJsonRepresentation;

public interface CcpMapTransform<V> {
	V transform(CcpJsonRepresentation values);

}

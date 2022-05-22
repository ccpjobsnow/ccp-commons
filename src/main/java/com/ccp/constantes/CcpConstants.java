package com.ccp.constantes;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpProcess;

public interface CcpConstants {
	CcpProcess returnEmpty = x -> new CcpMapDecorator();
	CcpProcess doNothing = x -> x;

}

package com.ccp.constantes;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpProcess;

public interface CcpConstants {
	CcpMapDecorator emptyJson = new CcpMapDecorator();
	CcpProcess returnEmpty = x -> CcpConstants.emptyJson;
	CcpProcess doNothing = x -> x;
}

package com.ccp.constantes;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpProcess;
import com.google.gson.Gson;

public interface CcpConstants {
	CcpMapDecorator emptyJson = new CcpMapDecorator();
	CcpProcess returnEmpty = x -> CcpConstants.emptyJson;
	CcpProcess doNothing = x -> x;
	Gson gson = new Gson();
	

}

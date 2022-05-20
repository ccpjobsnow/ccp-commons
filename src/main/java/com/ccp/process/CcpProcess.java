package com.ccp.process;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpProcess{
	
	CcpMapDecorator execute(CcpMapDecorator values);
}

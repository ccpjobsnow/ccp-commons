package com.ccp.especifications.instant.messenger;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpInstantMessenger {

	
	Long getMembersCount(CcpMapDecorator parameters);
	
	CcpMapDecorator sendMessage(CcpMapDecorator parameters);
	
	String getFileName(CcpMapDecorator parameters);
	
	String extractTextFromMessage(CcpMapDecorator parameters);
	


}

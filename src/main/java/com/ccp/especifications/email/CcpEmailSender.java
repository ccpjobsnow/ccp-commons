package com.ccp.especifications.email;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpEmailSender {

	void send(CcpMapDecorator emailParameters) ;
}

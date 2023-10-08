package com.ccp.especifications.db.dao;

import com.ccp.decorators.CcpMapDecorator;

public class CcpGetEntityId {

	private final CcpMapDecorator id;

	public CcpGetEntityId(CcpMapDecorator id) {
		this.id = id;
	}
	public CcpDaoProcedure toBeginProcedureAnd() {
		return new CcpDaoProcedure(this.id, new CcpMapDecorator());
	}


}

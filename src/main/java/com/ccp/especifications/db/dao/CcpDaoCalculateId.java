package com.ccp.especifications.db.dao;

import com.ccp.decorators.CcpMapDecorator;

public class CcpDaoCalculateId {

	private final CcpMapDecorator id;

	public CcpDaoCalculateId(CcpMapDecorator id) {
		this.id = id;
	}
	public CcpDaoProcedure toBeginProcedureAnd() {
		return new CcpDaoProcedure(this.id, new CcpMapDecorator());
	}


}

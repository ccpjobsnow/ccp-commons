package com.ccp.especifications.db.dao;

import com.ccp.decorators.CcpMapDecorator;

public class UseThisId {

	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;
	private final CcpDao dao;

	UseThisId(CcpMapDecorator id, CcpMapDecorator statements, CcpDao dao) {
		this.statements = statements;
		this.id = id;
		this.dao = dao;

	}
	
	public Procedure toBeginProcedureAnd() {
		return new Procedure(this.id, this.statements, this.dao);
	}


}

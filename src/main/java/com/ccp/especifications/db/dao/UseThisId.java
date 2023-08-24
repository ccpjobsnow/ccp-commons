package com.ccp.especifications.db.dao;

import com.ccp.decorators.CcpMapDecorator;

public class UseThisId {

	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;

	public UseThisId(CcpMapDecorator id, CcpMapDecorator statements) {
		this.statements = statements;
		this.id = id;

	}
	
	public Procedure toBeginProcedureAnd() {
		return new Procedure(this.id, this.statements);
	}


}

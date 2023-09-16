package com.ccp.especifications.db.dao;

import com.ccp.decorators.CcpMapDecorator;

public class CcpDaoNextStep {
	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;

	CcpDaoNextStep(CcpMapDecorator id, CcpMapDecorator statements) {
		this.id = id;
		this.statements = statements;

	}
	
	public CcpDaoFinally andFinally() {
		return new CcpDaoFinally(this.id, this.statements);
	}
	
	public CcpDaoProcedure and() {
		return new CcpDaoProcedure(this.id, this.statements);
	}
	
}

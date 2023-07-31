package com.ccp.especifications.db.dao;

import com.ccp.decorators.CcpMapDecorator;

public class NextStep {
	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;
	private final CcpDao dao;

	NextStep(CcpMapDecorator id, CcpMapDecorator statements, CcpDao dao) {
		this.id = id;
		this.statements = statements;
		this.dao = dao;

	}
	
	public Finally andFinally() {
		return new Finally(this.dao, this.id, this.statements);
	}
	
	public Procedure and() {
		return new Procedure(this.id, this.statements, this.dao);
	}
	
}

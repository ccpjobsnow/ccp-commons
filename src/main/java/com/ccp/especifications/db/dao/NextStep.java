package com.ccp.especifications.db.dao;

import com.ccp.decorators.CcpMapDecorator;

public class NextStep {
	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;

	NextStep(CcpMapDecorator id, CcpMapDecorator statements) {
		this.id = id;
		this.statements = statements;

	}
	
	public Finally andFinally() {
		return new Finally(this.id, this.statements);
	}
	
	public Procedure and() {
		return new Procedure(this.id, this.statements);
	}
	
}

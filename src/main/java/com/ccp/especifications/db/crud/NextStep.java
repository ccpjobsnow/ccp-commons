package com.ccp.especifications.db.crud;

import com.ccp.decorators.CcpMapDecorator;

public class NextStep {
	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;
	private final CcpDao crud;

	NextStep(CcpMapDecorator id, CcpMapDecorator statements, CcpDao crud) {
		this.id = id;
		this.statements = statements;
		this.crud = crud;

	}
	
	public Finally andFinally() {
		return new Finally(this.crud, this.id, this.statements);
	}
	
	public Procedure and() {
		return new Procedure(this.id, this.statements, this.crud);
	}
	
}

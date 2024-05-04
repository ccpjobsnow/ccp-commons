package com.ccp.especifications.db.crud;

import com.ccp.decorators.CcpJsonRepresentation;

public class CcpSelectNextStep {
	private final CcpJsonRepresentation id;
	private final CcpJsonRepresentation statements;

	CcpSelectNextStep(CcpJsonRepresentation id, CcpJsonRepresentation statements) {
		this.id = id;
		this.statements = statements;

	}
	
	public CcpSelectFinally andFinallyReturningThisFields(String... fields) {
		return new CcpSelectFinally(this.id, this.statements, fields);
	}
	
	public CcpSelectProcedure and() {
		return new CcpSelectProcedure(this.id, this.statements);
	}
	
}

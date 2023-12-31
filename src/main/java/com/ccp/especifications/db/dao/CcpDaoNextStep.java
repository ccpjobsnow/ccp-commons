package com.ccp.especifications.db.dao;

import com.ccp.decorators.CcpJsonRepresentation;

public class CcpDaoNextStep {
	private final CcpJsonRepresentation id;
	private final CcpJsonRepresentation statements;

	CcpDaoNextStep(CcpJsonRepresentation id, CcpJsonRepresentation statements) {
		this.id = id;
		this.statements = statements;

	}
	
	public CcpDaoFinally andFinallyReturningThisFields(String... fields) {
		return new CcpDaoFinally(this.id, this.statements, fields);
	}
	
	public CcpDaoProcedure and() {
		return new CcpDaoProcedure(this.id, this.statements);
	}
	
}

package com.ccp.especifications.db.dao;

import com.ccp.decorators.CcpJsonRepresentation;

public class CcpDaoLoadDataFromEntity {
	private final CcpJsonRepresentation id;
	private final CcpJsonRepresentation statements;

	CcpDaoLoadDataFromEntity(CcpJsonRepresentation id, CcpJsonRepresentation statements) {
		this.statements = statements;
		this.id = id;

	}

	public CcpDaoProcedure and() {
		return new CcpDaoProcedure(this.id, this.statements);
	}
	
	public CcpDaoFinally andFinally(String... fields) {
		return new CcpDaoFinally(this.id, this.statements, fields);
	}
	


}

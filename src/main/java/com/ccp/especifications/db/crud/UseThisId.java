package com.ccp.especifications.db.crud;

import com.ccp.decorators.CcpMapDecorator;

public class UseThisId {

	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;
	private final CcpDao crud;

	UseThisId(CcpMapDecorator id, CcpMapDecorator statements, CcpDao crud) {
		this.statements = statements;
		this.id = id;
		this.crud = crud;

	}
	
	public Procedure toBeginProcedureAnd() {
		return new Procedure(this.id, this.statements, this.crud);
	}


}

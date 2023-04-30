package com.ccp.especifications.db.crud;

import com.ccp.decorators.CcpMapDecorator;

public class UseThisId {

	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;
	private final CcpDbCrud crud;

	UseThisId(CcpMapDecorator id, CcpMapDecorator statements, CcpDbCrud crud) {
		this.statements = statements;
		this.id = id;
		this.crud = crud;

	}
	
	public Procedure toBeginProcedure() {
		return new Procedure(this.id, this.statements, this.crud);
	}


}

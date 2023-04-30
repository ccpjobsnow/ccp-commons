package com.ccp.especifications.db.crud;

import com.ccp.decorators.CcpMapDecorator;

public class LoadDataFromTable {
	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;
	private final CcpDbCrud crud;

	LoadDataFromTable(CcpMapDecorator id, CcpMapDecorator statements, CcpDbCrud crud) {
		this.statements = statements;
		this.crud = crud;
		this.id = id;

	}

	public Procedure andSo() {
		return new Procedure(this.id, this.statements, this.crud);
	}
	
	public Finally andFinally() {
		return new Finally(this.crud, this.id, this.statements);
	}
	


}

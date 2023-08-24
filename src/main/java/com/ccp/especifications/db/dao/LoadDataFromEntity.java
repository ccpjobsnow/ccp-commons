package com.ccp.especifications.db.dao;

import com.ccp.decorators.CcpMapDecorator;

public class LoadDataFromEntity {
	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;

	LoadDataFromEntity(CcpMapDecorator id, CcpMapDecorator statements) {
		this.statements = statements;
		this.id = id;

	}

	public Procedure andSo() {
		return new Procedure(this.id, this.statements);
	}
	
	public Finally andFinally() {
		return new Finally(this.id, this.statements);
	}
	


}

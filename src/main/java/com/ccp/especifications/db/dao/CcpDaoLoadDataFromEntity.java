package com.ccp.especifications.db.dao;

import com.ccp.decorators.CcpMapDecorator;

public class CcpDaoLoadDataFromEntity {
	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;

	CcpDaoLoadDataFromEntity(CcpMapDecorator id, CcpMapDecorator statements) {
		this.statements = statements;
		this.id = id;

	}

	public CcpDaoProcedure andSo() {
		return new CcpDaoProcedure(this.id, this.statements);
	}
	
	public CcpDaoFinally andFinally() {
		return new CcpDaoFinally(this.id, this.statements);
	}
	


}

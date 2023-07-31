package com.ccp.especifications.db.dao;

import com.ccp.decorators.CcpMapDecorator;

public class LoadDataFromEntity {
	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;
	private final CcpDao dao;

	LoadDataFromEntity(CcpMapDecorator id, CcpMapDecorator statements, CcpDao dao) {
		this.statements = statements;
		this.dao = dao;
		this.id = id;

	}

	public Procedure andSo() {
		return new Procedure(this.id, this.statements, this.dao);
	}
	
	public Finally andFinally() {
		return new Finally(this.dao, this.id, this.statements);
	}
	


}

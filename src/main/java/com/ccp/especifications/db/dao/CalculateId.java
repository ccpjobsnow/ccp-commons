package com.ccp.especifications.db.dao;

import com.ccp.decorators.CcpMapDecorator;

public class CalculateId {

	private final CcpMapDecorator id;

	public CalculateId(CcpMapDecorator id) {
		this.id = id;
	}
	public Procedure toBeginProcedureAnd() {
		return new Procedure(this.id, new CcpMapDecorator());
	}


}

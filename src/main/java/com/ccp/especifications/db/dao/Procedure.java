package com.ccp.especifications.db.dao;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpEntity;

public class Procedure {
	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;
	private final CcpDao dao;
	Procedure(CcpMapDecorator id, CcpMapDecorator statements, CcpDao dao) {
		this.statements = statements;
		this.dao = dao;
		this.id = id;
	}

	public LoadDataFromEntity loadThisIdFromEntity(CcpEntity entity) {
		CcpMapDecorator addToList = this.statements.addToList("statements", new CcpMapDecorator().put("entity", entity));
		return new LoadDataFromEntity(this.id, addToList, this.dao);
	}

	public FoundInEntity ifThisIdIsPresentInEntity(CcpEntity entity) {
		CcpMapDecorator put = new CcpMapDecorator().put("found", true).put("entity", entity);
		CcpMapDecorator addToList = this.statements.addToList("statements", put);
		return new FoundInEntity(this.id, addToList, this.dao);
	}

	public FoundInEntity ifThisIdIsNotPresentInEntity(CcpEntity entity) {
		CcpMapDecorator put = new CcpMapDecorator().put("found", false).put("entity", entity);
		CcpMapDecorator addToList = this.statements.addToList("statements", put);
		return new FoundInEntity(this.id, addToList, this.dao);
	}
 }

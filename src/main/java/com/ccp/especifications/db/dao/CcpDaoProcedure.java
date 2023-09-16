package com.ccp.especifications.db.dao;

import java.util.List;
import java.util.function.Function;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpEntity;

public class CcpDaoProcedure {
	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;
	CcpDaoProcedure(CcpMapDecorator id, CcpMapDecorator statements) {
		this.statements = statements;
		this.id = id;
	}

	public CcpDaoLoadDataFromEntity loadThisIdFromEntity(CcpEntity entity) {
		CcpMapDecorator addToList = this.statements.addToList("statements", new CcpMapDecorator().put("entity", entity));
		return new CcpDaoLoadDataFromEntity(this.id, addToList);
	}

	public CcpDaoFoundInEntity ifThisIdIsPresentInEntity(CcpEntity entity) {
		CcpMapDecorator put = new CcpMapDecorator().put("found", true).put("entity", entity);
		CcpMapDecorator addToList = this.statements.addToList("statements", put);
		return new CcpDaoFoundInEntity(this.id, addToList);
	}
	
	public CcpDaoFoundInEntity ifThisIdIsNotPresentInEntity(CcpEntity entity) {
		CcpMapDecorator put = new CcpMapDecorator().put("found", false).put("entity", entity);
		CcpMapDecorator addToList = this.statements.addToList("statements", put);
		return new CcpDaoFoundInEntity(this.id, addToList);
	}
	
	public CcpDaoNextStep executeAction(Function<CcpMapDecorator, CcpMapDecorator> action) {
		return this.addStatement("action", action);
	}
	
	private CcpDaoNextStep addStatement(String key, Object obj) {
		List<CcpMapDecorator> list = this.statements.getAsMapList("statements");
		list.add(new CcpMapDecorator().put(key, obj));
		CcpMapDecorator newStatements = this.statements.put("statements", list);
		return new CcpDaoNextStep(this.id, newStatements);
	}

 }

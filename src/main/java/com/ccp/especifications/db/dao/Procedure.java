package com.ccp.especifications.db.dao;

import java.util.List;
import java.util.function.Function;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpEntity;

public class Procedure {
	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;
	Procedure(CcpMapDecorator id, CcpMapDecorator statements) {
		this.statements = statements;
		this.id = id;
	}

	public LoadDataFromEntity loadThisIdFromEntity(CcpEntity entity) {
		CcpMapDecorator addToList = this.statements.addToList("statements", new CcpMapDecorator().put("entity", entity));
		return new LoadDataFromEntity(this.id, addToList);
	}

	public FoundInEntity ifThisIdIsPresentInEntity(CcpEntity entity) {
		CcpMapDecorator put = new CcpMapDecorator().put("found", true).put("entity", entity);
		CcpMapDecorator addToList = this.statements.addToList("statements", put);
		return new FoundInEntity(this.id, addToList);
	}
	
	public FoundInEntity ifThisIdIsNotPresentInEntity(CcpEntity entity) {
		CcpMapDecorator put = new CcpMapDecorator().put("found", false).put("entity", entity);
		CcpMapDecorator addToList = this.statements.addToList("statements", put);
		return new FoundInEntity(this.id, addToList);
	}
	
	public NextStep executeAction(Function<CcpMapDecorator, CcpMapDecorator> action) {
		return this.addStatement("action", action);
	}
	
	private NextStep addStatement(String key, Object obj) {
		List<CcpMapDecorator> list = this.statements.getAsMapList("statements");
		list.add(new CcpMapDecorator().put(key, obj));
		CcpMapDecorator newStatements = this.statements.put("statements", list);
		return new NextStep(this.id, newStatements);
	}

 }

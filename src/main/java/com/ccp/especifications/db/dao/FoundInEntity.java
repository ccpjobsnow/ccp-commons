package com.ccp.especifications.db.dao;

import java.util.List;
import java.util.function.Function;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpProcessStatus;


public class FoundInEntity {
	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;

	FoundInEntity(CcpMapDecorator id, CcpMapDecorator statements) {
		this.statements = statements;
		this.id = id;
	}

	public NextStep executeAction(Function<CcpMapDecorator, CcpMapDecorator> action) {
		return this.addStatement("action", action);
	}

	
	
	public NextStep returnStatus(CcpProcessStatus status) {
		return this.addStatement("status", status);
	}

	private NextStep addStatement(String key, Object obj) {
		List<CcpMapDecorator> list = this.statements.getAsMapList("statements");
		CcpMapDecorator lastStatement = list.get(list.size() - 1);
		CcpMapDecorator put = lastStatement.put(key, obj);
		List<CcpMapDecorator> subList = list.subList(0, list.size() - 1);
		subList.add(put);
		CcpMapDecorator newStatements = this.statements.put("statements", subList);
		return new NextStep(this.id, newStatements);
	}
	
}

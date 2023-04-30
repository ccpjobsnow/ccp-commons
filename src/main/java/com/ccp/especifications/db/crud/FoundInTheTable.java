package com.ccp.especifications.db.crud;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.process.CcpProcess;

public class FoundInTheTable {
	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;
	private final CcpDbCrud crud;

	FoundInTheTable(CcpMapDecorator id, CcpMapDecorator statements, CcpDbCrud crud) {
		this.statements = statements;
		this.crud = crud;
		this.id = id;
	}

	public NextStep thenDoAnAction(CcpProcess action) {
		return this.addStatement("action", action);
	}

	public NextStep thenReturnStatus(Integer status) {
		return this.addStatement("status", status);
	}

	private NextStep addStatement(String key, Object obj) {
		List<CcpMapDecorator> list = this.statements.getAsMapList("statements");
		CcpMapDecorator lastStatement = list.get(list.size() - 1);
		CcpMapDecorator put = lastStatement.put(key, obj);
		List<CcpMapDecorator> subList = list.subList(0, list.size() - 1);
		subList.add(put);
		CcpMapDecorator newStatements = this.statements.put("statements", subList);
		return new NextStep(this.id, newStatements, this.crud);
	}
	
}

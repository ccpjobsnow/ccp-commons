package com.ccp.especifications.db.crud;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpDbTable;

public class Procedure {
	// if an id "X" is found in table "Y", then return status "Z", so, if an id "A" is not found in table "B", then execute an action "A", so, load the data from table "B", finally ends this procedure retrieving the result
	private final CcpMapDecorator id;
	private final CcpMapDecorator statements;
	private final CcpDbCrud crud;
	Procedure(CcpMapDecorator id, CcpMapDecorator statements, CcpDbCrud crud) {
		this.statements = statements;
		this.crud = crud;
		this.id = id;
	}

	public LoadDataFromTable loadThisIdFromTable(CcpDbTable table) {
		CcpMapDecorator addToList = this.statements.addToList("statements", new CcpMapDecorator().put("table", table));
		return new LoadDataFromTable(this.id, addToList, this.crud);
	}

	public FoundInTheTable ifThisIdIsPresentInTable(CcpDbTable table) {
		CcpMapDecorator put = new CcpMapDecorator().put("found", true).put("table", table);
		CcpMapDecorator addToList = this.statements.addToList("statements", put);
		return new FoundInTheTable(this.id, addToList, this.crud);
	}

	public FoundInTheTable ifThisIdIsNotPresentInTable(CcpDbTable table) {
		CcpMapDecorator put = new CcpMapDecorator().put("found", false).put("table", table);
		CcpMapDecorator addToList = this.statements.addToList("statements", put);
		return new FoundInTheTable(this.id, addToList, this.crud);
	}
 }

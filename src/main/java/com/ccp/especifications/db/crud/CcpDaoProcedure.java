package com.ccp.especifications.db.crud;

import java.util.List;
import java.util.function.Function;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.CcpEntity;

public class CcpDaoProcedure {
	private final CcpJsonRepresentation id;
	private final CcpJsonRepresentation statements;
	CcpDaoProcedure(CcpJsonRepresentation id, CcpJsonRepresentation statements) {
		this.statements = statements;
		this.id = id;
	}

	public CcpDaoLoadDataFromEntity loadThisIdFromEntity(CcpEntity entity) {
		CcpJsonRepresentation addToList = this.statements.addToList("statements", CcpConstants.EMPTY_JSON.put("entity", entity));
		return new CcpDaoLoadDataFromEntity(this.id, addToList);
	}

	public CcpDaoFoundInEntity ifThisIdIsPresentInEntity(CcpEntity entity) {
		CcpJsonRepresentation put = CcpConstants.EMPTY_JSON.put("found", true).put("entity", entity);
		CcpJsonRepresentation addToList = this.statements.addToList("statements", put);
		return new CcpDaoFoundInEntity(this.id, addToList);
	}
	
	public CcpDaoFoundInEntity ifThisIdIsNotPresentInEntity(CcpEntity entity) {
		CcpJsonRepresentation put = CcpConstants.EMPTY_JSON.put("found", false).put("entity", entity);
		CcpJsonRepresentation addToList = this.statements.addToList("statements", put);
		return new CcpDaoFoundInEntity(this.id, addToList);
	}
	
	public CcpDaoNextStep executeAction(Function<CcpJsonRepresentation, CcpJsonRepresentation> action) {
		return this.addStatement("action", action);
	}
	
	private CcpDaoNextStep addStatement(String key, Object obj) {
		List<CcpJsonRepresentation> list = this.statements.getAsJsonList("statements");
		list.add(CcpConstants.EMPTY_JSON.put(key, obj));
		CcpJsonRepresentation newStatements = this.statements.put("statements", list);
		return new CcpDaoNextStep(this.id, newStatements);
	}

 }

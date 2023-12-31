package com.ccp.especifications.db.dao;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;

public class CcpGetEntityId {

	private final CcpJsonRepresentation id;

	public CcpGetEntityId(CcpJsonRepresentation id) {
		this.id = id;
	}
	public CcpDaoProcedure toBeginProcedureAnd() {
		return new CcpDaoProcedure(this.id, CcpConstants.EMPTY_JSON);
	}


}

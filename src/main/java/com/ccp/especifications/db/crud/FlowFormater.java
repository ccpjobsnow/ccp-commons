package com.ccp.especifications.db.crud;

import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.process.CcpProcessStatus;

class FlowFormater implements Function<CcpJsonRepresentation, CcpJsonRepresentation> {

	public static final FlowFormater INSTANCE = new FlowFormater();
	
	private FlowFormater() {}
	
	public CcpJsonRepresentation apply(CcpJsonRepresentation json) {
		CcpEntity ent = json.getAsObject("entity");
		String entityName = ent.getEntityName();
		CcpJsonRepresentation put2 = json.put("entity", entityName);
		CcpProcessStatus stats = json.getAsObject("status");
		CcpJsonRepresentation put3 = put2.put("statusName", stats.name()).put("statusNumber", stats.status());
		CcpJsonRepresentation removeField = put3.removeField("status");
		return removeField;
	}

}

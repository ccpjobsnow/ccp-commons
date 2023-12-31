package com.ccp.exceptions.db;

import java.util.List;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpEntityField;

@SuppressWarnings("serial")
public class CcpEntityMissingKeys extends RuntimeException {

	public CcpEntityMissingKeys(CcpEntity entity, List<CcpEntityField> keys, CcpJsonRepresentation object) {
		super("It is missing the keys '" + keys + "' from entity '" + entity.name() + "' in the object " + object );
	}
	
}

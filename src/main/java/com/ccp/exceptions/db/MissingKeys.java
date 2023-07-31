package com.ccp.exceptions.db;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpEntity;
import com.ccp.especifications.db.utils.CcpField;

@SuppressWarnings("serial")
public class MissingKeys extends RuntimeException {

	public MissingKeys(CcpEntity entity, List<CcpField> keys, CcpMapDecorator object) {
		super("It is missing the keys '" + keys + "' from entity '" + entity.name() + "' in the object " + object );
	}
	
}

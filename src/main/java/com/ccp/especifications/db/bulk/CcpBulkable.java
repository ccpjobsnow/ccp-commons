package com.ccp.especifications.db.bulk;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpDbTableField;

public interface CcpBulkable {

	String getId(CcpMapDecorator values);
	
	String name();
	
	CcpDbTableField[] getFields();
	
}

package com.ccp.exceptions.db.bulk;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;

@SuppressWarnings("serial")
public class CcpBulkErrors extends RuntimeException{

	public CcpBulkErrors(List<CcpMapDecorator> failedRecords) {
		super(failedRecords.toString());
	}
	
}

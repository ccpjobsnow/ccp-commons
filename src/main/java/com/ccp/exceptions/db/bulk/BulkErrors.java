package com.ccp.exceptions.db.bulk;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;

@SuppressWarnings("serial")
public class BulkErrors extends RuntimeException{

	public BulkErrors(List<CcpMapDecorator> failedRecords) {
		super(failedRecords.toString());
	}
	
}

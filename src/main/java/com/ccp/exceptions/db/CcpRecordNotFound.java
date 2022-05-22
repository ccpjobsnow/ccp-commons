package com.ccp.exceptions.db;

@SuppressWarnings("serial")
public class CcpRecordNotFound extends RuntimeException{

	public final String tableName;
	public final String id;
	public CcpRecordNotFound(String tableName, String id) {
		this.tableName = tableName;
		this.id = id;
	}
}

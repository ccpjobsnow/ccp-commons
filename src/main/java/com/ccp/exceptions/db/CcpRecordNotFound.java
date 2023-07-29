package com.ccp.exceptions.db;

@SuppressWarnings("serial")
public class CcpRecordNotFound extends RuntimeException{

	public final String entity;
	public final String id;
	public CcpRecordNotFound(String entity, String id) {
		this.entity = entity;
		this.id = id;
	}
}

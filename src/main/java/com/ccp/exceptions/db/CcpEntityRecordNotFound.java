package com.ccp.exceptions.db;

@SuppressWarnings("serial")
public class CcpEntityRecordNotFound extends RuntimeException{

	public final String entity;
	public final String id;
	public CcpEntityRecordNotFound(String entity, String id) {
		this.entity = entity;
		this.id = id;
	}
}

package com.ccp.especifications.db.utils;

public class CcpEntityDefaultField implements CcpEntityField{

	public static final CcpEntityDefaultField _index = new CcpEntityDefaultField(false, "_index");
	public static final CcpEntityDefaultField _id = new CcpEntityDefaultField(false, "_id");
	
	private final boolean primaryKey;
	private final String name;

	public CcpEntityDefaultField(boolean primaryKey, String name) {
		this.primaryKey = primaryKey;
		this.name = name;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public boolean isPrimaryKey() {
		return this.primaryKey;
	}

}

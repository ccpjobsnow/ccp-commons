package com.ccp.especifications.db.utils;

public class DefaultField implements CcpField{

	public static final DefaultField _index = new DefaultField(false, "_index");
	public static final DefaultField _id = new DefaultField(false, "_id");
	
	private final boolean primaryKey;
	private final String name;

	public DefaultField(boolean primaryKey, String name) {
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

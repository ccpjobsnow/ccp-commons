package com.ccp.especifications.json;

public interface CcpJson {

	
	String toJson(Object md);
	
	String asPrettyJson(Object md);
	
	<T> T fromJson(String md);
	
}

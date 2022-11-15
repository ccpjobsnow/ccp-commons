package com.ccp.especifications.file.bucket;

public interface CcpFileBucket {

	String read(String tenant, String bucketName, String fileName);

	void remove(String tenant, String bucketName, String fileName);
	
}

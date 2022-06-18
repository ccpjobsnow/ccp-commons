package com.ccp.especifications.file.bucket;

public interface CcpFileBucket {

	String read(String tenant, String bucketName, String fileName);
	
}

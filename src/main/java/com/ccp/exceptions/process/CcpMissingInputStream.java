package com.ccp.exceptions.process;

@SuppressWarnings("serial")
public class CcpMissingInputStream extends RuntimeException{
	public CcpMissingInputStream(String filePath) {
		super("The file '" + filePath + "' is missing");
	}
}

package com.ccp.process;

public class CcpSuccessStatus implements CcpProcessStatus{

	private CcpSuccessStatus() {
		
	}
	
	public static final CcpSuccessStatus INSTANCE = new CcpSuccessStatus();
	public int status() {
		return 200;
	}

	
	public String name() {
		return "success";
	}

}

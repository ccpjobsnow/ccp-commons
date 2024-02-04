package com.ccp.process;

public class CcpSuccessStatus implements CcpProcessStatus{

	
	public int status() {
		return 200;
	}

	
	public String name() {
		return "success";
	}

}

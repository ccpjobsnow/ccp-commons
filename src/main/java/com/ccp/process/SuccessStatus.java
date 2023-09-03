package com.ccp.process;

public class SuccessStatus implements CcpProcessStatus{

	@Override
	public int status() {
		return 200;
	}

	@Override
	public String name() {
		return "success";
	}

}

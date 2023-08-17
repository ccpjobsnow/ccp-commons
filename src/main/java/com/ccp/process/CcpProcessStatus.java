package com.ccp.process;

public interface CcpProcessStatus {
	int status();
	CcpProcessStatus nextStep = () -> 200;
}

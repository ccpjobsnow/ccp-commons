package com.ccp.process;

public interface CcpProcessStatus {
	int asNumber();
	String name();
	default void verifyStatus(int actualStatus) {
		int expectedStatus = this.asNumber();
		
		boolean correctStatus = expectedStatus == actualStatus;
		
		if(correctStatus) {
			return;
		}
		String testName = this.name();
		String msg = String.format("In the test '%s' it was expected the status '%s', but status '%s' was received", testName, expectedStatus, actualStatus);
		throw new RuntimeException(msg);
	}
}

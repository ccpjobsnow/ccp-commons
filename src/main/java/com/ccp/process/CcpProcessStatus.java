package com.ccp.process;

public interface CcpProcessStatus {
	int status();
	String name();
	CcpProcessStatus NOT_FOUND = new CcpProcessStatus() {
		
		@Override
		public int status() {
			return 404;
		}
		
		@Override
		public String name() {
			return "NOT_FOUND";
		}
	};
	CcpProcessStatus REDIRECT = new CcpProcessStatus() {
		
		@Override
		public int status() {
			return 301;
		}
		
		@Override
		public String name() {
			return "REDIRECT";
		}
	};
	
	default void verifyStatus(int actualStatus) {
		int expectedStatus = this.status();
		
		boolean correctStatus = expectedStatus == actualStatus;
		
		if(correctStatus) {
			return;
		}
		String testName = this.name();
		String msg = String.format("In the test '%s' it was expected the status '%s', but status '%s' was received", testName, expectedStatus, actualStatus);
		throw new RuntimeException(msg);
	}
}

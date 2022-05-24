package com.ccp.decorators;

public class CcpNumberDecorator {
	public final String content;

	protected CcpNumberDecorator(String content) {
		this.content = content;
	}

	public boolean isLong() {
		try {
			
			Long.valueOf(this.content);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isDouble() {
		try {
			
			Double.valueOf(this.content);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}

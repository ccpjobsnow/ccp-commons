package com.ccp.decorators;

public class CcpNumberDecorator {
	public final double content;

	protected CcpNumberDecorator(String content) {
		this.content = Double.valueOf(content);
	}

	public String toString() {
		return "" + this.content;
	}
	public boolean isGreaterThan(int x) {
		return this.content > x ;
	}

	public boolean isGreaterOrEqualsThan(int x) {
		return this.content >= x ;
	}

	public boolean isLessThan(int x) {
		return this.content < x ;
	}

	public boolean isLessOrEqualsThan(int x) {
		return this.content <= x ;
	}

	public boolean isEqualsTo(int x) {
		return this.content == x ;
	}

}

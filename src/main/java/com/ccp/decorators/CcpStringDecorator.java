package com.ccp.decorators;

public class CcpStringDecorator {

	public final String content;
	
	public CcpStringDecorator(String content) {
		this.content = content;
	}
	
	public CcpEmailDecorator email() {
		return new CcpEmailDecorator(this.content);
	}
	
	public CcpFileDecorator file() {
		return new CcpFileDecorator(this.content);
	}
	
	public CcpHashDecorator hash() {
		return new CcpHashDecorator(this.content);
	}
	
	public CcpNumberDecorator number() {
		return new CcpNumberDecorator(this.content);
	}
	
	public CcpTextDecorator text() {
		return new CcpTextDecorator(this.content);
	}
	
	public CcpUrlDecorator url() {
		return new CcpUrlDecorator(this.content);
	}
	
	public CcpMapDecorator map() {
		return new CcpMapDecorator(this.content);
	}
	
	public CcpPasswordDecorator password() {
		return new CcpPasswordDecorator(this.content);
	}
}

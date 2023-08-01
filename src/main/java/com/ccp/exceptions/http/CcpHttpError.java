package com.ccp.exceptions.http;

import com.ccp.decorators.CcpMapDecorator;

@SuppressWarnings("serial")
public class CcpHttpError extends RuntimeException {
	public final CcpMapDecorator entity;

	public CcpHttpError(CcpMapDecorator entity) {
		super(entity.toString());
		this.entity = entity;
	}
	
	public CcpHttpError() {
		this.entity = new CcpMapDecorator();
	}
	
	public CcpHttpError addDetail(String key, Object value) {
		return new CcpHttpError(this.entity.put(key, value));
	}
}
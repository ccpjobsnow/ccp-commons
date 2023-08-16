package com.ccp.especifications.http;

import java.util.List;

import com.ccp.decorators.CcpMapDecorator;

public interface CcpHttpResponseType {
	CcpHttpResponseTransform<List<CcpMapDecorator>> listRecord = response -> response.asListRecord();
	CcpHttpResponseTransform<CcpMapDecorator> singleRecord = response -> response.asSingleJson();
	CcpHttpResponseTransform<byte[]> byteArray = response -> response.httpResponse.getBytes();
	CcpHttpResponseTransform<List<Object>> listObject = response -> response.asListObject();
	CcpHttpResponseTransform<String> string = response -> response.httpResponse;
	CcpHttpResponseTransform<String> base64 = response -> response.asBase64();
	

}

package com.ccp.decorators;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

public class CcpInputStreamDecorator {
	
	private final String content;

	protected CcpInputStreamDecorator(String content) {
		this.content = content;
	}
	
	public String toString() {
		return this.content;
	}

	public InputStream classLoader() {
		try {
			Class<? extends CcpInputStreamDecorator> class1 = this.getClass();
			ClassLoader classLoader = class1.getClassLoader();
			URL resource = classLoader.getResource(this.content);
			InputStream stream = resource.openStream();
			return stream;
		} catch (Exception e) {
			throw new RuntimeException("The file '" + this.content + "' is missing", e);
		}
	}
	
	public InputStream file() {
		try {
			FileInputStream fileInputStream = new FileInputStream(this.content);
			return fileInputStream;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public InputStream byteArray() {
		byte[] bytes = this.content.getBytes();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		return byteArrayInputStream;
	}
}

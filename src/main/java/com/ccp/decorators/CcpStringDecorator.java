package com.ccp.decorators;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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
	
	public CcpInputStreamDecorator inputStreamFrom() {
		return new CcpInputStreamDecorator(this.content);
	}
	
	public CcpMapDecorator propertiesFileFromClassLoader() {
		InputStream is = this.inputStreamFrom().classLoader();
		return new CcpMapDecorator(is);
	}
	
	public CcpMapDecorator jsonFileFromClassLoader() {
		InputStream is = this.inputStreamFrom().classLoader();
		StringBuilder sb = new StringBuilder();
		InputStreamReader in = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(in);
		String read;
		try {
			while ((read=br.readLine()) != null) {
				sb.append(read);
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		CcpMapDecorator response = new CcpMapDecorator(sb.toString());
		return response;
	}
	
	public String toString() {
		return this.content;
	}
}

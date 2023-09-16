package com.ccp.decorators;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CcpPropertiesDecorator {

	private final CcpInputStreamDecorator cisd;

	protected CcpPropertiesDecorator(String content) {
		this.cisd = new CcpInputStreamDecorator(content);
	}

	private CcpMapDecorator getMap(InputStream is) {
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
	
	public CcpMapDecorator environmentVariables() {
		InputStream is = this.cisd.environmentVariables();
		CcpMapDecorator result = this.getMap(is);
		return result;
	}

	public CcpMapDecorator classLoader() {
		InputStream is = this.cisd.classLoader();
		CcpMapDecorator result = this.getMap(is);
		return result;
	}

	public CcpMapDecorator file() {
		InputStream is = this.cisd.file();
		CcpMapDecorator result = this.getMap(is);
		return result;
	}

	public CcpMapDecorator environmentVariablesOrClassLoaderOrFile() {
		InputStream is = this.cisd.fromEnvironmentVariablesOrClassLoaderOrFile();
		CcpMapDecorator result = this.getMap(is);
		return result;
	}
	
	
	
}

package com.ccp.decorators;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CcpPropertiesDecorator {

	private final CcpInputStreamDecorator cisd;

	protected CcpPropertiesDecorator(String content) {
		this.cisd = new CcpInputStreamDecorator(content);
	}

	private CcpMapDecorator getMap(InputStream is) {

		Properties properties = new Properties();
		try {
			properties.load(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		CcpMapDecorator response = new CcpMapDecorator(properties);
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

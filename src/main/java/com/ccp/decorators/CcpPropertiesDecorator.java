package com.ccp.decorators;

import java.io.InputStream;

public class CcpPropertiesDecorator {

	private final CcpInputStreamDecorator cisd;

	protected CcpPropertiesDecorator(String content) {
		this.cisd = new CcpInputStreamDecorator(content);
	}

	private CcpMapDecorator getMapInInputStream(InputStream is) {
		CcpMapDecorator response = new CcpMapDecorator(is);
		return response;

	}
	
	public CcpMapDecorator environmentVariables() {
		InputStream is = this.cisd.environmentVariables();
		CcpMapDecorator result = this.getMapInInputStream(is);
		return result;
	}

	public CcpMapDecorator classLoader() {
		InputStream is = this.cisd.classLoader();
		CcpMapDecorator result = this.getMapInInputStream(is);
		return result;
	}

	public CcpMapDecorator file() {
		InputStream is = this.cisd.file();
		CcpMapDecorator result = this.getMapInInputStream(is);
		return result;
	}

	public CcpMapDecorator environmentVariablesOrClassLoaderOrFile() {
		InputStream is = this.cisd.fromEnvironmentVariablesOrClassLoaderOrFile();
		CcpMapDecorator result = this.getMapInInputStream(is);
		return result;
	}
	
	
	
}

package com.ccp.decorators;

import java.io.InputStream;

public class CcpPropertiesDecorator {

	private final CcpInputStreamDecorator cisd;

	protected CcpPropertiesDecorator(String content) {
		this.cisd = new CcpInputStreamDecorator(content);
	}

	private CcpJsonRepresentation getMapInInputStream(InputStream is) {
		CcpJsonRepresentation response = new CcpJsonRepresentation(is);
		return response;

	}
	
	public CcpJsonRepresentation environmentVariables() {
		InputStream is = this.cisd.environmentVariables();
		CcpJsonRepresentation result = this.getMapInInputStream(is);
		return result;
	}

	public CcpJsonRepresentation classLoader() {
		InputStream is = this.cisd.classLoader();
		CcpJsonRepresentation result = this.getMapInInputStream(is);
		return result;
	}

	public CcpJsonRepresentation file() {
		InputStream is = this.cisd.file();
		CcpJsonRepresentation result = this.getMapInInputStream(is);
		return result;
	}

	public CcpJsonRepresentation environmentVariablesOrClassLoaderOrFile() {
		InputStream is = this.cisd.fromEnvironmentVariablesOrClassLoaderOrFile();
		CcpJsonRepresentation result = this.getMapInInputStream(is);
		return result;
	}
	
	
	
}

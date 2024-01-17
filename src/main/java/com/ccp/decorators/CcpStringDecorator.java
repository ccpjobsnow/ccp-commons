package com.ccp.decorators;

import java.util.Collection;
import java.util.function.Consumer;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.json.CcpJsonHandler;

public class CcpStringDecorator {

	public final String content;

	public CcpStringDecorator(CcpJsonRepresentation json, String key) {
		this.content = json.getAsString(key);
	}

	
	public CcpStringDecorator(String content) {
		this.content = content;
	}
	
	public CcpEmailDecorator email() {
		return new CcpEmailDecorator(this.content);
	}
	
	public CcpFileDecorator file() {
		return new CcpFileDecorator(this.content);
	}

	public CcpFolderDecorator folder() {
		return new CcpFolderDecorator(this.content);
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
	
	public CcpJsonRepresentation map() {
		return new CcpJsonRepresentation(this.content);
	}
	
	public CcpPasswordDecorator password() {
		return new CcpPasswordDecorator(this.content);
	}
	
	public CcpInputStreamDecorator inputStreamFrom() {
		return new CcpInputStreamDecorator(this.content);
	}
	
	public CcpPropertiesDecorator propertiesFrom() {
		CcpPropertiesDecorator ccpPropertiesDecorator = new CcpPropertiesDecorator(this.content);
		return ccpPropertiesDecorator;
	}
	
	public boolean isValidJSon() {
		CcpJsonHandler json = CcpDependencyInjection.getDependency(CcpJsonHandler.class);
		boolean validJson = json.isValidJson(this.content);
		return validJson;
	}
	
	public String toString() {
		return this.content;
	}
	
	@SuppressWarnings("unused")
	public boolean isList() {
		boolean valid = this.isValid(x ->  {
			Collection<?> fromJson = CcpDependencyInjection.getDependency(CcpJsonHandler.class).fromJson(x);
		});
		return valid;
		
	}
	@SuppressWarnings("unused")
	public boolean isLongNumber() {
		boolean valid = this.isValid(x -> {
			Object obj = x.endsWith(".0") ? Double.valueOf(x) : Long.valueOf(x);
		});
		return valid;
	}

	public boolean isDoubleNumber() {
		boolean valid = this.isValid(x -> Double.valueOf(x));
		return valid;
	}

	public boolean isBoolean() {
		boolean valid = this.isValid(x -> {
			if ("true".equalsIgnoreCase(x)) {
				return;
			}
			if ("false".equalsIgnoreCase(x)) {
				return;
			}
			throw new RuntimeException();
		});
		return valid;
	}
	
	
	private boolean isValid(Consumer<String>  consumer) {
		try {
			consumer.accept(this.content);;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}

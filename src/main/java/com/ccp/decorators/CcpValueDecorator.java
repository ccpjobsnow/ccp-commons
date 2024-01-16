package com.ccp.decorators;

import java.util.Collection;
import java.util.function.Consumer;

import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.json.CcpJsonHandler;

public class CcpValueDecorator {

	public final String content;
	
	public CcpValueDecorator(CcpJsonRepresentation json, String key) {
		this.content = json.getAsString(key);
	}
	
	public CcpValueDecorator(String content) {
		this.content = content;
	}



	public boolean isLongNumber() {
		boolean valid = this.isValid(x -> Double.valueOf(x));
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
	
	public boolean isJson() {
		boolean valid = this.isValid(x -> CcpJsonRepresentation.getMap(x));
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
	
	@SuppressWarnings("unused")
	public boolean isList() {
		boolean valid = this.isValid(x ->  {
			Collection<?> fromJson = CcpDependencyInjection.getDependency(CcpJsonHandler.class).fromJson(x);
		});
		return valid;
		
	}

}

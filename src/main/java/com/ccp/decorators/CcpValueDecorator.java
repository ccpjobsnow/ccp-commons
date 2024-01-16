package com.ccp.decorators;

import java.util.function.Consumer;

public class CcpValueDecorator {

	public final String content;
	
	public CcpValueDecorator(CcpJsonRepresentation json, String key) {
		this.content = json.getAsString(key);
	}
	
	public CcpValueDecorator(String content) {
		this.content = content;
	}



	public boolean isLongNumber() {
		boolean valid = this.isValid(x -> Long.valueOf(x));
		return valid;
	}

	public boolean isDoubleNumber() {
		boolean valid = this.isValid(x -> Double.valueOf(x));
		return valid;
	}

	public boolean isBoolean() {
		boolean valid = this.isValid(x -> Boolean.valueOf(x));
		return valid;
	}
	
	public boolean isValidInnerJson() {
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

}

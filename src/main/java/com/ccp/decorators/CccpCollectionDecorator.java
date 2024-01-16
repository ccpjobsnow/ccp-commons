package com.ccp.decorators;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class CccpCollectionDecorator implements Iterable<Object>{

	private final Collection<Object> content;
	
	public CccpCollectionDecorator(Collection<Object> content) {
		this.content = content;
	}
	
	public CccpCollectionDecorator(Object...array) {
		this.content = Arrays.asList(array);
	}
	
	
	public CccpCollectionDecorator(CcpJsonRepresentation json, String key) {
		List<Object> asObjectList = json.getAsObjectList(key);
		this.content = asObjectList;
	}

	public boolean isLongNumberList() {
		
		boolean validList = this.isValidList(x -> Long.valueOf(x));
		return validList;
	}

	public boolean isDoubleNumberList() {
		
		boolean validList = this.isValidList(x -> Double.valueOf(x));
		return validList;
	}

	public boolean isBooleanList() {
		
		boolean validList = this.isValidList(x -> Boolean.valueOf(x));
		return validList;
	}
	public boolean isJsonList() {
		
		boolean validList = this.isValidList(x -> CcpJsonRepresentation.getMap(x));
		return validList;
	}

	private boolean isValidList(Consumer<String> consumer) {
		
		for (Object object : this.content) {
			try {
				consumer.accept("" + object);
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	
	@Override
	public Iterator<Object> iterator() {
		return this.content.iterator();
	}
	
	
	
	
}

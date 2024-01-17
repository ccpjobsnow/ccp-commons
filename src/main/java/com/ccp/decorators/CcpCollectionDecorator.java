package com.ccp.decorators;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class CcpCollectionDecorator implements Iterable<Object>{

	private final Collection<Object> content;
	
	public CcpCollectionDecorator(Collection<Object> content) {
		this.content = content;
	}
	
	public CcpCollectionDecorator(Object...array) {
		this.content = Arrays.asList(array);
	}
	
	
	public CcpCollectionDecorator(CcpJsonRepresentation json, String key) {
		List<Object> asObjectList = json.getAsObjectList(key);
		this.content = asObjectList;
	}

	public boolean isLongNumberList() {
		
		boolean validList = this.isValidList(x -> x.isLongNumber());
		return validList;
	}

	public boolean isDoubleNumberList() {
		
		boolean validList = this.isValidList(x -> x.isDoubleNumber());
		return validList;
	}

	public boolean isBooleanList() {
		
		boolean validList = this.isValidList(x -> x.isBoolean());
		return validList;
	}
	public boolean isJsonList() {
		
		boolean validList = this.isValidList(x -> x.text().isValidSingleJson());
		return validList;
	}

	private boolean isValidList(Predicate<CcpStringDecorator> predicate) {
		
		for (Object object : this.content) {
			CcpStringDecorator t = new CcpStringDecorator("" + object);
			boolean failed = predicate.test(t) == false;
			if(failed) {
				return false;
			}
		}
		return true;
	}

	
	@Override
	public Iterator<Object> iterator() {
		return this.content.iterator();
	}
	
	
	public boolean isEmpty() {
		return this.content.isEmpty();
	}
	
	public CcpNumberDecorator size() {
		return new CcpNumberDecorator("" + this.content.size());
	}
}

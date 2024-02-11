package com.ccp.validation;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import com.ccp.decorators.CcpCollectionDecorator;
import com.ccp.decorators.CcpJsonRepresentation;

public class IfTheyAre {
	public final CcpJsonRepresentation content;
	public final String[] fields;
	public IfTheyAre(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}
	
	public boolean textsThenEachOneIsContainedAtTheList(String ...args) {
		
		List<String> asList = Arrays.asList(args);
		
		for (String field : this.fields) {
			
			String asString = this.content.getAsString(field);
			
			boolean notAllowedValue = asList.contains(asString) == false;
			if(notAllowedValue) {
				return false;
			}
		}
		return true;
	}
	
	public boolean textsThenEachOneIsContainedAtTheList(Collection<Object> args) {
		CcpCollectionDecorator ccpCollectionDecorator = new CcpCollectionDecorator(args);
		String[] array = ccpCollectionDecorator.toArray();
		boolean result = this.textsThenEachOneIsContainedAtTheList(array);
		return result;
	}	

	public RangeSize textsThenEachOneHasTheSizeThatIs() {
		Function<String[], String[]> arrayProducer = fields -> {
			String[] result = new String[fields.length];
			int k = 0;
			for (String field : fields) {
				String asString = this.content.getAsString(field);
				result[k++] = "" + asString.length();
			}
			return result;
		};
		return new RangeSize(arrayProducer, this.fields);
	}
	public RangeSize numbersThenEachOneIs() {
		Function<String[], String[]> arrayProducerOfItems = ItIsTrueThatTheFollowingFields.getArrayProducerOfItems(this.content);
		return new RangeSize(arrayProducerOfItems, this.fields);
	}

	public boolean numbersThenEachOneIsContainedAtTheList(Double... args) {

		List<Double> asList = Arrays.asList(args);
		
		for (String field : this.fields) {

			Double value = this.content.getAsDoubleNumber(field);
			
			boolean notAllowedValue = asList.contains(value) == false;
			if(notAllowedValue) {
				return false;
			}
			
		}
		return true;
	}

	public boolean numbersThenEachOneIsContainedAtTheList(Collection<Object> args) {
		CcpCollectionDecorator ccpCollectionDecorator = new CcpCollectionDecorator(args);
		Double[] array = ccpCollectionDecorator.toArray();
		boolean result = this.numbersThenEachOneIsContainedAtTheList(array);
		return result;
	}

}

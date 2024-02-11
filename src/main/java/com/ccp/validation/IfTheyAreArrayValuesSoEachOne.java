package com.ccp.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpCollectionDecorator;
import com.ccp.decorators.CcpJsonRepresentation;

public class IfTheyAreArrayValuesSoEachOne {
	public final CcpJsonRepresentation content;
	public final String[] fields;
	
	
	public IfTheyAreArrayValuesSoEachOne(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}
	public boolean hasNonDuplicatedItems() {
		for (String field : this.fields) {
			boolean hasDuplicatedItems = this.content.getAsArrayMetadata(field).hasNonDuplicatedItems() == false;
			if(hasDuplicatedItems) {
				return false;
			}
		}
		return true;
	}
	public AreOfTheType isOfTheType() {

		Function<String[], String[]> arrayProducerOfArrays = ItIsTrueThatTheFollowingFields.getArrayProducerOfArrays(this.content);
		return new AreOfTheType(arrayProducerOfArrays, this.fields);
	}
	public RangeSize hasTheSizeThatIs() {
		Function<String[], String[]> arrayProducer = fields -> {
			String[] result = new String[fields.length];
			int k = 0;
			for (String field : fields) {
				int size = this.content.getAsObjectList(field).size();
				result[k++] = "" + size;
			}
			return result;
		};
		return new RangeSize(arrayProducer, this.fields);
	}
	public boolean isTextAndItIsContainedAtTheList(String...args) {
		
		List<String> asList = Arrays.asList(args);	
		
		for (String field : this.fields) {
			List<String> asStringList = this.content.getAsStringList(field);
			for (String string : asStringList) {
				boolean notAllowedValue = asList.contains(string) == false;
				if(notAllowedValue) {
					return false;
				}
			}
		
		}
		return true;
	}
	
	public boolean isTextAndItIsContainedAtTheList(Collection<Object> args) {
		CcpCollectionDecorator ccpCollectionDecorator = new CcpCollectionDecorator(args);
		String[] array = ccpCollectionDecorator.toArray();
		boolean result = this.isTextAndItIsContainedAtTheList(array);
		return result;
	}
	
	
	public boolean isNumberAndItIsContainedAtTheList(Double...args) {
		
		List<Double> asList = Arrays.asList(args);

		for (String field : this.fields) {
			List<Double> collect = this.content.getAsStringList(field).stream().map(x -> Double.valueOf(x)).collect(Collectors.toList());
			for (Double number : collect) {
				boolean notAllowedValue = asList.contains(number) == false;
				if(notAllowedValue) {
					return false;
				}
			}
		}
		
		return true;
	}
	public boolean isNumberAndItIsContainedAtTheList(Collection<Object> args) {
		CcpCollectionDecorator ccpCollectionDecorator = new CcpCollectionDecorator(args);
		Double[] array = ccpCollectionDecorator.toArray();
		boolean result = this.isNumberAndItIsContainedAtTheList(array);
		return result;
	
	}	
	
	public RangeSize isTextAndHasSizeThatIs() {
		Function<String[], String[]> xxx = fields ->{
			List<String> list = new ArrayList<>();

			for (String field : fields) {
				List<String> asStringList = this.content.getAsStringList(field);
				list.addAll(asStringList.stream().map(x -> "" + x.length()).collect(Collectors.toList()));
			}
			
			return list.toArray(new String[list.size()]);
		};
	
		return new RangeSize(xxx, this.fields);
	}
	public RangeSize isNumberAndItIs() {
		Function<String[], String[]> arrayProducer = fields -> {
			List<String> list = new ArrayList<String>();
			for (String field : fields) {
				List<String> asStringList = this.content.getAsStringList(field);
				list.addAll(asStringList);
			}
			String[] result = list.toArray(new String[list.size()]);
			return result;
		};

		return new RangeSize(arrayProducer, this.fields);
	}

	
}

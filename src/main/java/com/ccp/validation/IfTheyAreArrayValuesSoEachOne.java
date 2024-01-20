package com.ccp.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
		for (String field : this.fields) {
			CcpCollectionDecorator asArrayMetadata = this.content.getAsArrayMetadata(field);
			boolean isNotContained = asArrayMetadata.isContainedAtTheList(x -> "" + x, (Object[]) args) == false;
			if(isNotContained) {
				return false;
			}
		}
		return true;
	}
	public boolean isNumberAndItIsContainedAtTheList(double...args) {
		Double[] doubles = new Double[args.length];
		int k = 0;
		for (double d : args) {
			doubles[k++] = d;
		}

		for (String field : this.fields) {
			CcpCollectionDecorator asArrayMetadata = this.content.getAsArrayMetadata(field);
			boolean isNotContained = asArrayMetadata.isContainedAtTheList(x -> Double.valueOf("" + x), (Object[]) doubles) == false;
			if(isNotContained) {
				return false;
			}
		}
		return true;
	}
	
	
	public IfTheyAreArrayValuesSoEachOne isTextAnd() {
		return new IfTheyAreArrayValuesSoEachOne(this.content, this.fields);
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

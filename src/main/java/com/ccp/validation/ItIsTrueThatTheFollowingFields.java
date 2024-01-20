package com.ccp.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpNumberDecorator;

public class ItIsTrueThatTheFollowingFields {

	public final CcpJsonRepresentation content;
	public final String[] fields;

	public ItIsTrueThatTheFollowingFields(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}

	public AreOfTheType areOfTheType() {
		Function<String[], String[]> arrayProducerOfItems = ItIsTrueThatTheFollowingFields.getArrayProducerOfItems(this.content);
		return new AreOfTheType(arrayProducerOfItems, this.fields);
	}

	public IfTheyAreArrayValuesSoEachOne ifTheyAreArrayValuesThenEachOne() {
		return new IfTheyAreArrayValuesSoEachOne(this.content, this.fields);
	}

	public IfTheyAre ifTheyAre() {
		return new IfTheyAre(this.content, this.fields);
	}
	
	public static Function<String[], String[]> getArrayProducerOfArrays(CcpJsonRepresentation content) {
		Function<String[], String[]> arrayProcucer = fields ->{
			List<String> list = new ArrayList<String>();
			for (String field : fields) {
				List<String> asStringList = content.getAsStringList(field);
				list.addAll(asStringList);
			}
			String[] array = list.toArray(new String[list.size()]);
			return array;
		};
		
		return arrayProcucer;

	}
	
	public static Function<String[], String[]> getArrayProducerOfItems(CcpJsonRepresentation content) {
		Function<String[], String[]> arrayProducer = fields -> {
			String [] result = new String[fields.length];	
			int k = 0;
			for (String field : fields) {
				String value = content.getAsString(field);
				result[k++] = value;
			}
			return result;
		};
		
		return arrayProducer;

	}

}
interface Tester{
	boolean test(CcpNumberDecorator d, int limit);
}

package com.ccp.validation;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpNumberDecorator;
import com.ccp.decorators.CcpJsonRepresentation.ValueExtractor;

public class ItIsTruthThatTheFollowingFields {

	public final CcpJsonRepresentation content;
	public final String[] fields;

	public ItIsTruthThatTheFollowingFields(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}

	public AreOfTheType areOfTheType() {
		return new AreOfTheType(this.content, this.fields);
	}

	public IfTheyAreArrayValuesSoEachOne ifTheyAreArrayValuesSoEachOne() {
		return new IfTheyAreArrayValuesSoEachOne(this.content, this.fields);
	}

	public IfTheyAre ifTheyAre() {
		return new IfTheyAre(this.content, this.fields);
	}
	
	
}
interface Tester{
	boolean test(CcpNumberDecorator d, int limit);
}
class X {
	public final CcpJsonRepresentation content;
	
	public final String[] fields;

	public X(CcpJsonRepresentation content, String[] fields, ValueExtractor<String> valueExtractor) {
		super();
		this.content = content;
		this.fields = fields;
		this.valueExtractor = valueExtractor;
	}

	ValueExtractor<String> valueExtractor = (x, json) -> json.getAsString(x);

	public boolean equalsTo(int limit) {
		boolean test = X.test((d, x) -> d.equalsTo(x), this.valueExtractor, this.content, limit, this.fields);
		return test;
	}
	public boolean hasNonDuplicatedItems() {
		for (String field : this.fields) {
			boolean hasNotDuplicatedItems = this.content.getAsArrayMetadata(field).hasNotDuplicatedItems();
			if(hasNotDuplicatedItems == false) {
				return false;
			}
		}
		return true;
	}

	protected static boolean test(  Tester testing, ValueExtractor<String> valueExtractor, CcpJsonRepresentation content, int limit,  String...fields) {
		for (String field : fields) {
			String asString = content.get(field, valueExtractor );
			CcpNumberDecorator d = new CcpNumberDecorator(asString);
			boolean failed = testing.test(d, limit) == false;
			if(failed) {
				return false;
			}
		}
		return true;
	}

}

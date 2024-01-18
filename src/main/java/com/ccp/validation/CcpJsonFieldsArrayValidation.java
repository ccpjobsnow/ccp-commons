package com.ccp.validation;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.decorators.CcpJsonRepresentation.ValueExtractor;
import com.ccp.decorators.CcpNumberDecorator;

public class CcpJsonFieldsArrayValidation {

	public final CcpJsonRepresentation content;
	
	public final String[] fields;

	
	protected CcpJsonFieldsArrayValidation(CcpJsonRepresentation content, String[] fields) {
		this.content = content;
		this.fields = fields;
	}

	public boolean withHasNotDuplicatedItems() {
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

interface Tester{
	boolean test(CcpNumberDecorator d, int limit);
}


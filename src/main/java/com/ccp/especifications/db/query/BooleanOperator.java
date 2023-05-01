package com.ccp.especifications.db.query;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpDbTableField;

abstract class BooleanOperator extends Component{

	protected Set<Object> items = new LinkedHashSet<>();

	BooleanOperator(Component parent, String name) {
		super(parent, name);
	}

	public <T extends BooleanOperator> T term(CcpDbTableField field, Object value) {
		T addCondition = this.addCondition(field.name(), value, "term");
		return addCondition;
	}

	public <T extends BooleanOperator> T prefix(CcpDbTableField field, Object value) {
		T addCondition = this.addCondition(field.name(), value, "prefix");
		return addCondition;
	}

	public <T extends BooleanOperator> T match(CcpDbTableField field, Object value) {
		T addCondition = this.addCondition(field.name(), value, "match");
		return addCondition;
	}

	public <T extends BooleanOperator> T matchPhrase(CcpDbTableField field, Object value) {
		T addCondition = this.addCondition(field.name(), value, "match_phrase");
		return addCondition;
	}

	public <T extends BooleanOperator> T match(CcpDbTableField field, Object value, double boost, String operator) {
		T addCondition = this.addCondition(field.name(), value, "match", boost, operator);
		return addCondition;
	}

	public <T extends BooleanOperator> T matchPhrase(CcpDbTableField field, Object value, double boost) {
		T addCondition = this.addCondition(field.name(), value, "match_phrase", boost, "");
		return addCondition;
	}

	public <T extends BooleanOperator> T exists(String field) {
		
		
		T addCondition = this.addCondition("field", field, "exists");
		return addCondition;
	}

	@SuppressWarnings("unchecked")
	protected <T extends BooleanOperator> T addCondition(String field, Object value, String key) {
		BooleanOperator clone = this.copy();
		if(value == null) {
			return (T)clone;
		}
		
		Map<String, Object> map = new CcpMapDecorator().put(field, value).getContent();
		Map<String, Object> outerMap = new CcpMapDecorator().put(key, map).getContent();
		
		clone.items.addAll(this.items);
		clone.items.add(outerMap);
		return (T)clone;
	}

	
	@SuppressWarnings("unchecked")
	protected <T extends BooleanOperator> T addCondition(String field, Object value, String key, double boost, String operator) {
		BooleanOperator clone = this.copy();
		if(value == null) {
			return (T)clone;
		}
		
		CcpMapDecorator put = new CcpMapDecorator().put("query", value).put("boost", boost);
		if(operator != null && operator.trim().isEmpty() == false) {
			put = put.put("operator", operator);
		}
		Map<String, Object> map = put.getContent();
		Map<String, Object> mapField = new CcpMapDecorator().put(field, map).getContent();	
		new CcpMapDecorator().put(key, mapField).getContent();
		Map<String, Object> outerMap = new CcpMapDecorator().put(key, mapField).getContent();
		
		clone.items.addAll(this.items);
		clone.items.add(outerMap);
		return (T)clone;
	}

	@Override
	Object getValue() {
		return new ArrayList<>(this.items);
	}

	@SuppressWarnings("unchecked")
	<T extends Component> T addChild(Component child) {
		
		BooleanOperator copy = this.copy();
		copy.items.addAll(this.items);
		Object childValue = child.getValue();
		Map<String, Object> childContent = new CcpMapDecorator().put(child.name, childValue).getContent();
		copy.items.add(childContent);
		return (T)copy;
	}

	@SuppressWarnings("unchecked")
	protected <T extends Component> T copy() {
		BooleanOperator instanceCopy = this.getInstanceCopy();

		instanceCopy.name = this.name;
		
		instanceCopy.parent = this.parent.copy();
		
		instanceCopy.items.addAll(this.items);
		
		return (T)instanceCopy;
	}

	public Range startRange() {
		return new Range(this);
	}

	
	@Override
	public boolean hasChildreen() {
		return this.items.isEmpty() == false;
	}
}

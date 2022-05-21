package com.ccp.especifications.db.query;

public class FieldRange extends Component{

	FieldRange(Component parent, String name) {
		super(parent, name);
	}

	@SuppressWarnings("unchecked")
	protected FieldRange getInstanceCopy() {
		return new FieldRange(this.parent, this.name);
	}

	
	private FieldRange putOperator(String operatorName, Object value) {
		FieldRange copy = this.copy();
		copy.values = copy.values.put(operatorName, value);
		return copy;
	}
	
	public FieldRange lessThan(Object value) {
		return this.putOperator("lt", value);
	}

	public FieldRange lessThanEquals(Object value) {
		return this.putOperator("lte", value);
	}

	public FieldRange greaterThan(Object value) {
		return this.putOperator("gt", value);
	}

	public FieldRange greaterThanEquals(Object value) {
		return this.putOperator("gte", value);
	}
	
	public Range endFieldRangeAndBackToRange() {
		return this.parent.addChild(this);
	}

}

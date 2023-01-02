package com.ccp.especifications.db.query;

import com.ccp.especifications.db.utils.CcpDbTableField;

public final class ShouldNot  extends BooleanOperator {

	ShouldNot(Component parent) {
		super(parent, "should_not");
	}
	@Override
	@SuppressWarnings("unchecked")
	public ShouldNot prefix(CcpDbTableField field, Object value) {
		return super.prefix(field, value);
	}

	public Bool endShouldNotAndBackToBool() {
		Component copy = this.parent.copy();
		Bool addChild = copy.addChild(this);
		return addChild;
	}
	@SuppressWarnings("unchecked")
	protected <T extends Component> T getInstanceCopy() {
		return (T)new ShouldNot(this.parent);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public ShouldNot matchPhrase(CcpDbTableField field, Object value) {
		return super.matchPhrase(field, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ShouldNot term(CcpDbTableField field, Object value) {
		return super.term(field, value);
	}
	
	@SuppressWarnings("unchecked")
	public ShouldNot exists(String field) {
		return super.exists(field);
	}

	public Bool startBool() {
		return new Bool(this);
	}

}

package com.ccp.especifications.db.query;

import com.ccp.especifications.db.table.CcpDbTableField;

public final class Must extends BooleanOperator{

	Must(Component parent) {
		super(parent, "must");
	}
	
	public Bool endMustAndBackToBool() {
		return this.parent.addChild(this);
	}
	@Override
	@SuppressWarnings("unchecked")
	public Must matchPhrase(CcpDbTableField field, Object value) {
		return super.matchPhrase(field, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Must prefix(CcpDbTableField field, Object value) {
		return super.prefix(field, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Must term(CcpDbTableField field, Object value) {
		return super.term(field, value);
	}

	@SuppressWarnings("unchecked")
	protected <T extends Component> T getInstanceCopy() {
		return (T)new Must(this.parent);
	}
	

	@SuppressWarnings("unchecked")
	public Must exists(String field) {
		return super.exists(field);
	}
	public Bool startBool() {
		return new Bool(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Must match(CcpDbTableField field, Object value) {
		return super.match(field, value);
	}
}

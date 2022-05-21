package com.ccp.especifications.db.query;

public final class MustNot extends BooleanOperator{

	 MustNot(Component parent) {
		super(parent, "must_not");
	}
	
	public Bool endMustNotAndBackToBool() {
		return this.parent.addChild(this);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public MustNot prefix(String field, Object value) {
		return super.prefix(field, value);
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public MustNot matchPhrase(String field, Object value) {
		return super.matchPhrase(field, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public MustNot term(String field, Object value) {
		return super.term(field, value);
	}

	@SuppressWarnings("unchecked")
	protected <T extends Component> T getInstanceCopy() {
		return (T)new MustNot(this.parent);
	}
	
	@SuppressWarnings("unchecked")
	public MustNot exists(String field) {
		return super.exists(field);
	}
	public Bool startBool() {
		return new Bool(this);
	}


}

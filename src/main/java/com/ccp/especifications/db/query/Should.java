package com.ccp.especifications.db.query;

public final class Should  extends BooleanOperator {

	Should(Component parent) {
		super(parent, "should");
		
	}
	@Override
	@SuppressWarnings("unchecked")
	public Should prefix(String field, Object value) {
		return super.prefix(field, value);
	}

	Should setMinimumShouldMatch(int minimumShouldMatch) {
		Should copy = this.copy();
		copy.parent.values = copy.parent.values.put("minimum_should_match", minimumShouldMatch);
		return copy;
	}
	
	public Bool endShouldAndBackToBool() {
		Component copy = this.parent.copy();
		Bool addChild = copy.addChild(this);
		return addChild;
	}
	
	public Should matchPhrase2(String field, Object value) {
		return super.matchPhrase(field, value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Should match(String field, Object value) {
		return super.match(field, value);
	}
	
	@SuppressWarnings("unchecked")
	public Should matchPhrase(String field, Object value, double boost) {
		Should addCondition = this.addCondition(field, value, "match_phrase", boost, "");
		return addCondition;
	}
	
	@SuppressWarnings("unchecked")
	public Should match(String field, Object value, double boost, String operator) {
		Should addCondition = this.addCondition(field, value, "match", boost, operator);
		return addCondition;
	}



	@Override
	@SuppressWarnings("unchecked")
	public Should term(String field, Object value) {
		return super.term(field, value);
	}
	@SuppressWarnings("unchecked")
	protected <T extends Component> T getInstanceCopy() {
		return (T)new Should(this.parent);
	}
	
	@SuppressWarnings("unchecked")
	public Should exists(String field) {
		return super.exists(field);
	}	
	
	public Bool startBool() {
		return new Bool(this);
	}

}

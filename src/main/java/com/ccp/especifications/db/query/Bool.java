package com.ccp.especifications.db.query;

public final class Bool  extends Component{

	 Bool(Component parent) {
		super(parent, "bool");
	}

	public Filter startFilter() {
		return new Filter(this);
	}
	
	public Must startMust() {
		return new Must(this);
	}

	public Should startShould(int minimumShouldMatch) {
		return new Should(this).setMinimumShouldMatch(minimumShouldMatch);
	}

	public MustNot startMustNot() {
		return new MustNot(this);
	}

	public ShouldNot startShouldNot() {
		return new ShouldNot(this);
	}

	public Should endBoolAndBackToShould() {
		return this.parent.addChild(this);
	}

	public Must endBoolAndBackToMust() {
		return this.parent.addChild(this);
	}

	public ShouldNot endBoolAndBackToShouldNot() {
		return this.parent.addChild(this);
	}

	public MustNot endBoolAndBackToMustNot() {
		return this.parent.addChild(this);
	}
	
	public Filter endBoolAndBackToFilter() {
		return this.parent.addChild(this);
	}
	
	public Query endBoolAndBackToQuery() {
		return this.parent.addChild(this);
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends Component> T getInstanceCopy() {
		return (T)new Bool(this.parent);
	}

}

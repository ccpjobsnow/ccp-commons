package com.ccp.especifications.db.query;

public final class Query  extends Component {

	Query(Component parent) {
		super(parent, "query");
	}

	public Bool startBool() {
		return new Bool(this);
	}

	public ElasticQuery endQueryAndBackToRequest() {
		return this.parent.addChild(this);
	}
	@SuppressWarnings("unchecked")
	protected <T extends Component> T getInstanceCopy() {
		return (T)new Query(this.parent);
	}
	
}

package com.ccp.especifications.db.query;

public final class Filter  extends Component {
	Filter(Component parent) {
		super(parent, "filter");
	}

	 public Bool startBool() {
		return new Bool(this);
	}
	
	public Bool endFilterAndBackToBool() {
		return this.parent.addChild(this);
	}

	@SuppressWarnings("unchecked")
	protected <T extends Component> T getInstanceCopy() {
		return (T)new Filter(this.parent);
	}
}
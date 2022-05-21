package com.ccp.especifications.db.query;

public class Range extends Component {

	Range(Component parent) {
		super(parent, "range"); 
	}
   
	@SuppressWarnings("unchecked")
	@Override
	protected <T extends Component> T getInstanceCopy() {
		return (T)new Range(this.parent);
	}

	public FieldRange startFieldRange(String fieldName) {
		return new FieldRange(this, fieldName);
	}
	
	public SimplifiedQuery endRangeAndBackToSimplifiedQuery() {
		return this.parent.addChild(this);
	}
	public Should endRangeAndBackToShould() {
		return this.parent.addChild(this);
	}

	public Must endRangeAndBackToMust() {
		return this.parent.addChild(this);
	}

	public ShouldNot endRangeAndBackToShouldNot() {
		return this.parent.addChild(this);
	}

	public MustNot endRangeAndBackToMustNot() {
		return this.parent.addChild(this);
	}

}

package com.ccp.especifications.db.query;

import java.util.Map;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.utils.CcpDbTableField;

public final class BucketAggregation extends Component {
	
	private final CcpDbTableField fieldName;
	private final long size;
	
	BucketAggregation(Component parent, String name, CcpDbTableField fieldName, long size) {
		super(parent, name);
		this.fieldName = fieldName;
		this.size = size;
	}

	public Aggregations endTermsBuckedAndBackToAggregations() {
		Aggregations addChild = this.getStatisRequest("size", "terms");
		return addChild;
	}

	public Aggregations endHistogramBuckedAndBackToAggregations() {
		Aggregations addChild = this.getStatisRequest("interval", "histogram");
		return addChild;
	}

	private Aggregations getStatisRequest(String p1, String p2) {
		Component copy = this.copy();
		Map<String, Object> content = new CcpMapDecorator().put("field", this.fieldName).put(p1, this.size).getContent();
		copy.values = copy.values.put(p2, content);
		Aggregations addChild = this.parent.addChild(copy);
		return addChild;
	}
	
	public Aggregations startAggregations() {
		return new Aggregations(this);
	}

	@SuppressWarnings("unchecked")
	protected <T extends Component> T getInstanceCopy() {
		return (T)new BucketAggregation(this.parent, this.name, this.fieldName, this.size);
	}

}

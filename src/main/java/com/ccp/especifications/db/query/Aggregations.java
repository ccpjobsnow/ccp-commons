package com.ccp.especifications.db.query;

import java.util.Map;

import com.ccp.decorators.CcpMapDecorator;


public final class Aggregations extends Component{
	
	 Aggregations(Component parent) {
		super(parent, "aggs");
	}

	public ElasticQuery endAggregationsAndBackToRequest() {
		return this.parent.addChild(this);
	}

	public BucketAggregation endAggregationsAndBackToBucket() {
		return this.parent.addChild(this);
	}

	public Aggregations addMinAggregation(String aggregationName, String fieldName) {
		Aggregations copy = this.createAggregation(aggregationName, fieldName, "min");
		return copy;
	}

	private Aggregations createAggregation(String aggregationName, String fieldName, String key) {
		Aggregations copy = this.copy();
		Map<String, Object> c1 = new CcpMapDecorator().put("field", fieldName).getContent();
		Map<String, Object> c2 = new CcpMapDecorator().put(key, c1).getContent();
		copy.values = copy.values.put(aggregationName, c2);
		return copy;
	}
	public Aggregations addMaxAggregation(String aggregationName, String fieldName) {
		Aggregations copy = this.createAggregation(aggregationName, fieldName, "max");
		return copy;
	}

	public Aggregations addAvgAggregation(String aggregationName, String fieldName) {
		Aggregations copy = this.createAggregation(aggregationName, fieldName, "avg");
		return copy;
	}

	public BucketAggregation startBucket(String bucketName, String fieldName, long size) {
		BucketAggregation bucketAggregation = new BucketAggregation(this, bucketName, fieldName, size);
		
		
		return bucketAggregation;
				
	}

	@SuppressWarnings("unchecked")
	protected <T extends Component> T getInstanceCopy() {
		return (T)new Aggregations(this.parent);
	}

	public Aggregations addSumAggregation(String aggregationName, String fieldName) {
		Aggregations copy = this.createAggregation(aggregationName, fieldName, "sum");
		return copy;
	}

}

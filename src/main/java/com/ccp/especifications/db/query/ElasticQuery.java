package com.ccp.especifications.db.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ccp.decorators.CcpMapDecorator;


public class ElasticQuery extends Component{
	
	public ElasticQuery() { 
		super(null, "");
	}

	public Query startQuery() {
		return new Query(this);
	}
	
	public SimplifiedQuery startSimplifiedQuery() {
		return new SimplifiedQuery(this); 
	}
	
	public Aggregations startAggregations() {
		return new Aggregations(this);
	}
	
	public ElasticQuery addAscSorting(String fields) {
		ElasticQuery sort = this.addSorting("asc", fields);
		return sort;
	}

	public ElasticQuery addDescSorting(String... fields) {
		ElasticQuery sort = this.addSorting("desc", fields);
		return sort;
	}
	
	public ElasticQuery addSorting(String sortType, String... fields) {
		ElasticQuery sort = this;
		
		for (String field : fields) {
			sort = sort.sort(field, sortType);
		}
		
		return sort;
		
	}

	private ElasticQuery sort(String fieldName, String sortType) {
		ElasticQuery copy = this.copy();
		Map<String, Object> content = new CcpMapDecorator().put(fieldName, sortType).getContent();
		
		List<Object> asList = Arrays.asList(content);

		if(copy.values.content.containsKey("sort")) {
			List<Object> sort = copy.values.getAsObjectList("sort");
			asList = new ArrayList<>(sort);
			asList.add(content);
		}
		copy.values = copy.values.put("sort", asList);
		return copy;
	}
	@SuppressWarnings("unchecked")
	protected <T extends Component> T getInstanceCopy() {
		return (T)new ElasticQuery();
	}

	public CcpQueryExecutorDecorator selectFrom(CcpDbQueryExecutor requestExecutor, String... resourcesNames) {
		return new CcpQueryExecutorDecorator(requestExecutor, this, resourcesNames);
	}
	
	public ElasticQuery setScrollId(String scrollId) {
		ElasticQuery clone = super.putProperty("scroll_id", scrollId);
		return clone;
		
	}
	
	public ElasticQuery setSize(int size) {
		ElasticQuery clone = super.putProperty("size", size);
		return clone;
	}
	
	public ElasticQuery maxResults() {
		ElasticQuery clone = super.putProperty("size", 10000);
		return clone;
	}

	public ElasticQuery zeroResults() {
		ElasticQuery clone = super.putProperty("size", 0);
		return clone;
	}
	

	public ElasticQuery setFrom(int from) {
		ElasticQuery clone = super.putProperty("from", from);
		return clone;
	}
	
	

	public ElasticQuery setScrollTime(String scrollTime) {
		ElasticQuery clone = super.putProperty("scroll", scrollTime);
		return clone;
	}
	public ElasticQuery matchAll() {
		ElasticQuery clone = super.putProperty("query", new CcpMapDecorator().put("match_all", new CcpMapDecorator().content).content);
		return clone;
	}

	
}

package com.ccp.especifications.db.query;

import java.util.List;
import java.util.function.Consumer;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.dependency.injection.CcpDependencyInjection;

public class CcpQueryExecutorDecorator {

	private final CcpDbQueryExecutor requestExecutor = CcpDependencyInjection.getDependency(CcpDbQueryExecutor.class); 
	
	private final String[] resourcesNames;
	
	private final ElasticQuery elasticQuery;

	protected CcpQueryExecutorDecorator(ElasticQuery elasticQuery, String... resourcesNames) {
		this.resourcesNames = resourcesNames;
		this.elasticQuery = elasticQuery;
	}

	public CcpMapDecorator getResultAsPackage(String url, String method, int expectedStatus,  String... array) {
		return this.requestExecutor.getResultAsPackage(url, method, expectedStatus, this.elasticQuery, this.resourcesNames, array);
	}

	public CcpMapDecorator getTermsStatis(String fieldName) {
		return this.requestExecutor.getTermsStatis(this.elasticQuery, this.resourcesNames, fieldName);
	}

	public CcpMapDecorator delete() {
		return this.requestExecutor.delete(this.elasticQuery, this.resourcesNames);
	}

	public CcpMapDecorator update(CcpMapDecorator newValues) {
		return this.requestExecutor.update(this.elasticQuery, this.resourcesNames, newValues);
	}

	public void consumeQueryResult(String scrollTime, int size,
			Consumer<List<CcpMapDecorator>> consumer, String... fields) {
		this.requestExecutor.consumeQueryResult(this.elasticQuery, this.resourcesNames, scrollTime, size, consumer, fields);
	}

	public long total() {
		return this.requestExecutor.total(this.elasticQuery, this.resourcesNames);
	}

	public List<CcpMapDecorator> getResultAsList(String... fieldsToSearch) {
		return this.requestExecutor.getResultAsList(this.elasticQuery, this.resourcesNames, fieldsToSearch);
	}

	public CcpMapDecorator getResultAsMap(String field) {
		return this.requestExecutor.getResultAsMap(this.elasticQuery, this.resourcesNames, field);
	}

	public CcpMapDecorator getMap(String field) {
		return this.requestExecutor.getMap(this.elasticQuery, this.resourcesNames, field);
	}

	public CcpMapDecorator getAggregations() {
		return requestExecutor.getAggregations(this.elasticQuery, this.resourcesNames);
	};
	
	
	
	
}

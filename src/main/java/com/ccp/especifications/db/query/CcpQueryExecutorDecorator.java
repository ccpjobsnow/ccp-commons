package com.ccp.especifications.db.query;

import java.util.List;
import java.util.function.Consumer;

import com.ccp.decorators.CcpMapDecorator;

public class CcpQueryExecutorDecorator {

	private final CcpQueryExecutor requestExecutor;
	
	private final String[] resourcesNames;

	protected CcpQueryExecutorDecorator(CcpQueryExecutor requestExecutor, String... resourcesNames) {
		this.requestExecutor = requestExecutor;
		this.resourcesNames = resourcesNames;
	}

	public CcpMapDecorator getTermsStatis(String fieldName) {
		return requestExecutor.getTermsStatis(this.resourcesNames, fieldName);
	}

	public CcpMapDecorator delete() {
		return requestExecutor.delete(this.resourcesNames);
	}

	public CcpMapDecorator update(CcpMapDecorator newValues) {
		return requestExecutor.update(this.resourcesNames, newValues);
	}

	public void consumeQueryResult(String scrollTime, int size,
			Consumer<List<CcpMapDecorator>> consumer, String... fields) {
		requestExecutor.consumeQueryResult(this.resourcesNames, scrollTime, size, consumer, fields);
	}

	public long total() {
		return requestExecutor.total(this.resourcesNames);
	}

	public List<CcpMapDecorator> getResultAsList(String... fieldsToSearch) {
		return requestExecutor.getResultAsList(this.resourcesNames, fieldsToSearch);
	}

	public CcpMapDecorator getResultAsMap(String field) {
		return requestExecutor.getResultAsMap(this.resourcesNames, field);
	}

	public CcpMapDecorator getResultAsPackage(String... array) {
		return requestExecutor.getResultAsPackage(this.resourcesNames, array);
	}

	public CcpMapDecorator getMap(String field) {
		return requestExecutor.getMap(this.resourcesNames, field);
	}

	public CcpMapDecorator getAggregations() {
		return requestExecutor.getAggregations(this.resourcesNames);
	};
	
	
	
	
}

package com.ccp.especifications.db.query;

import java.util.List;
import java.util.function.Consumer;

import com.ccp.decorators.CcpMapDecorator;
public interface CcpQueryExecutor {

	CcpMapDecorator getTermsStatis(CcpDbQueryOptions elasticQuery, String[] resourcesNames, String fieldName);

	CcpMapDecorator delete(CcpDbQueryOptions elasticQuery, String[] resourcesNames);
	
	CcpMapDecorator update(CcpDbQueryOptions elasticQuery, String[] resourcesNames, CcpMapDecorator newValues) ;
	
	void consumeQueryResult(CcpDbQueryOptions elasticQuery, String[] resourcesNames, String scrollTime, int size, Consumer<List<CcpMapDecorator> > consumer, String...fields);

	long total(CcpDbQueryOptions elasticQuery, String[] resourcesNames);

	List<CcpMapDecorator> getResultAsList(CcpDbQueryOptions elasticQuery, String[] resourcesNames, String... fieldsToSearch);
	
	CcpMapDecorator getResultAsMap(CcpDbQueryOptions elasticQuery, String[] resourcesNames, String field);

	CcpMapDecorator getResultAsPackage(String url, String method, int expectedStatus, CcpDbQueryOptions elasticQuery, String[] resourcesNames, String ...array);

	CcpMapDecorator getMap(CcpDbQueryOptions elasticQuery, String[] resourcesNames, String field);
	
	CcpMapDecorator getAggregations(CcpDbQueryOptions elasticQuery, String[] resourcesNames) ;	
}

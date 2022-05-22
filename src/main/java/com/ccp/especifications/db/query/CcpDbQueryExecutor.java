package com.ccp.especifications.db.query;

import java.util.List;
import java.util.function.Consumer;

import com.ccp.decorators.CcpMapDecorator;
public interface CcpDbQueryExecutor {

	CcpMapDecorator getTermsStatis(ElasticQuery elasticQuery, String[] resourcesNames, String fieldName);

	CcpMapDecorator delete(ElasticQuery elasticQuery, String[] resourcesNames);
	
	CcpMapDecorator update(ElasticQuery elasticQuery, String[] resourcesNames, CcpMapDecorator newValues) ;
	
	void consumeQueryResult(ElasticQuery elasticQuery, String[] resourcesNames, String scrollTime, int size, Consumer<List<CcpMapDecorator> > consumer, String...fields);

	long total(ElasticQuery elasticQuery, String[] resourcesNames);

	List<CcpMapDecorator> getResultAsList(ElasticQuery elasticQuery, String[] resourcesNames, String... fieldsToSearch);
	
	CcpMapDecorator getResultAsMap(ElasticQuery elasticQuery, String[] resourcesNames, String field);

	CcpMapDecorator getResultAsPackage(ElasticQuery elasticQuery, String[] resourcesNames, String ...array);

	CcpMapDecorator getMap(ElasticQuery elasticQuery, String[] resourcesNames, String field);
	
	CcpMapDecorator getAggregations(ElasticQuery elasticQuery, String[] resourcesNames) ;	
}

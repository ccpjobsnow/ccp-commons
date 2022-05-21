package com.ccp.especifications.db.query;

import java.util.List;
import java.util.function.Consumer;

import com.ccp.decorators.CcpMapDecorator;
public interface CcpQueryExecutor {
	CcpMapDecorator getTermsStatis(String[] resourcesNames, String fieldName);

	CcpMapDecorator delete(String[] resourcesNames);
	
	CcpMapDecorator update(String[] resourcesNames, CcpMapDecorator newValues) ;
	

	void consumeQueryResult(String[] resourcesNames, String scrollTime, int size, Consumer<List<CcpMapDecorator> > consumer, String...fields);

	long total(String[] resourcesNames);
	

	List<CcpMapDecorator> getResultAsList(String[] resourcesNames, String... fieldsToSearch);
	

	CcpMapDecorator getResultAsMap(String[] resourcesNames, String field);

	CcpMapDecorator getResultAsPackage(String[] resourcesNames, String ...array);

	
	CcpMapDecorator getMap(String[] resourcesNames, String field);
	
	CcpMapDecorator getAggregations(String[] resourcesNames) ;	
}

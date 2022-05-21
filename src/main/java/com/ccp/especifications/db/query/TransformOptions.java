package com.ccp.especifications.db.query;

import java.util.List;
import java.util.function.Consumer;

import com.ccp.decorators.CcpMapDecorator;
interface TransformOptions {
	CcpMapDecorator getTermsStatis(String fieldName);

	CcpMapDecorator delete();
	
	CcpMapDecorator update(CcpMapDecorator newValues) ;
	

	void consumeQueryResult(String scrollTime, int size, Consumer<List<CcpMapDecorator> > consumer, String...fields);

	long total();
	

	List<CcpMapDecorator> getResultAsList(String... fieldsToSearch);
	

	CcpMapDecorator getResultAsMap(String field);

	CcpMapDecorator getResultAsPackage(String ...array);

	
	CcpMapDecorator getMap(String field);
	
	CcpMapDecorator getAggregations() ;	
}

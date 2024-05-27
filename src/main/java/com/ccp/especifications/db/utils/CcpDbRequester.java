package com.ccp.especifications.db.utils;

import java.util.List;
import java.util.function.Consumer;

import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.especifications.db.bulk.CcpBulkOperationResult;
import com.ccp.especifications.http.CcpHttpResponseTransform;
import com.ccp.exceptions.db.CcpIncorrectEntityFields;

public interface CcpDbRequester {

	<V> V executeHttpRequest(String trace, String url, String method, Integer expectedStatus, CcpJsonRepresentation body, String[] resources, CcpHttpResponseTransform<V> transformer);

	<V> V executeHttpRequest(String trace, String url, String method,  Integer expectedStatus, String body, CcpJsonRepresentation headers, CcpHttpResponseTransform<V> transformer);

	<V> V executeHttpRequest(String trace, String url, String method, CcpJsonRepresentation flows, CcpJsonRepresentation body, CcpHttpResponseTransform<V> transformer);

	<V> V executeHttpRequest(String trace, String url, String method, Integer expectedStatus, CcpJsonRepresentation body, CcpHttpResponseTransform<V> transformer);

	List<CcpBulkOperationResult> executeDatabaseSetup(String pathToJavaClasses, String hostFolder, String pathToCreateEntityScript,	Consumer<CcpIncorrectEntityFields> whenIsIncorrectMapping,	Consumer<Throwable> whenOccursAnError);

	CcpJsonRepresentation getConnectionDetails();

	String getFieldNameToEntity();

	String getFieldNameToId();
}

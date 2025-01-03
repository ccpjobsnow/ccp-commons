package com.ccp.decorators;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ccp.constantes.CcpOtherConstants;
import com.ccp.constantes.CcpStringConstants;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.json.CcpJsonHandler;
import com.ccp.especifications.password.CcpPasswordHandler;
import com.ccp.exceptions.json.JsonFieldNotFound;
import com.ccp.validation.ItIsTrueThatTheFollowingFields;
 
public final class CcpJsonRepresentation {

	public final Map<String, Object> content;
	
	protected CcpJsonRepresentation() {
		this.content = new HashMap<>();
	}
	
	public static CcpJsonRepresentation getEmptyJson() {
		return new CcpJsonRepresentation();
	}
	
	public CcpJsonRepresentation(InputStream is) {

		this.content = new HashMap<>();
		String result = this.extractJson(is);
		CcpJsonHandler handler = CcpDependencyInjection.getDependency(CcpJsonHandler.class);

		boolean validJson = handler.isValidJson(result);
		
		if(validJson) {
			CcpJsonHandler json = CcpDependencyInjection.getDependency(CcpJsonHandler.class);
			Map<String, Object> map = json.fromJson(result);
			this.content.putAll(map);
			return;
		}

		
		Properties props = new Properties();
		
		try {
			byte[] bytes = result.getBytes();
			ByteArrayInputStream inStream = new ByteArrayInputStream(bytes);
			props.load(inStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		Set<Object> keySet = props.keySet();
		for (Object key : keySet) {
			Object value = props.get(key);
			this.content.put("" + key, value);
		}
	}

	private String extractJson(InputStream is) {
		InputStreamReader in = new InputStreamReader(is);
		String result = new BufferedReader(in).lines().collect(Collectors.joining("\n"));
		return result;
	}
	
	public CcpJsonRepresentation(Throwable e) {
		this(getErrorDetails(e).content);
	}

	public CcpJsonRepresentation(String json) {
		this(getMap(json));
	}

	static Map<String, Object> getMap(String json) {
		CcpJsonHandler handler = CcpDependencyInjection.getDependency(CcpJsonHandler.class);
		try {
			Map<String, Object> fromJson = handler.fromJson(json);
			return fromJson;
			
		} catch (Exception e) {
			throw new RuntimeException("The following json is an invalid json: " + json , e);
		}
	}
	
	public CcpJsonRepresentation(Map<String, Object> content) {
		if(content == null) {
			throw new RuntimeException("this json is null");
		}
		this.content = Collections.unmodifiableMap(content);
	}

	private static CcpJsonRepresentation getErrorDetails(Throwable e) {

		CcpJsonRepresentation jr = CcpOtherConstants.EMPTY_JSON;
		
		if(e == null) {
			return jr; 
		}
		
		Throwable cause = e.getCause();
		String message = e.getMessage();
		StackTraceElement[] st = e.getStackTrace();
		List<String> stackTrace = new ArrayList<>();
		for (StackTraceElement ste : st) {
			int lineNumber = ste.getLineNumber();
			String methodName = ste.getMethodName();
			String fileName = ste.getFileName();
			if(fileName == null) {
				continue;
			}
			String key = fileName.replace(".java", "") + "." + methodName + ":" + lineNumber+ "\n";
			stackTrace.add(key);
		}
		
		CcpJsonRepresentation causeDetails = getErrorDetails(cause);

		jr = jr.put("type", e.getClass().getName()).put("stackTrace", stackTrace.toString()).put("msg", message).put("cause", causeDetails);
		return jr;
	}
	
	public Long getAsLongNumber(String field) {
		
		Object object = this.content.get(field);
		try {
			return Double.valueOf("" + object).longValue();
		} catch (Exception e) {
			throw new RuntimeException("The value '" + object + "' from the field '" + field + " is not a long");
		}
	}

	public Integer getAsIntegerNumber(String field) {
		
		Object object = this.content.get(field);

		try {
			return Double.valueOf("" + object).intValue();
		} catch (Exception e) {
			throw new RuntimeException("The value '" + object + "' from the field '" + field + "' is not a integer");
		}
		
	}
	

	public boolean getAsBoolean(String field) {
		
		try {
			String asString = this.getAsString(field);
			return Boolean.valueOf(asString.toLowerCase());
		} catch (Exception e) {
			return false;
		}
	}
	
	
	public Double getAsDoubleNumber(String field) {
		
		Object object = this.content.get(field);
		try {
			return Double.valueOf("" + object);
		} catch (Exception e) {
			throw new RuntimeException("The value '" + object + "' from the field '" + field + "' is not a double");
		}
	}
	
	public CcpJsonRepresentation putFilledTemplate(String fieldToSearch, String fieldToPut) {
		
		String asString = this.getAsString(fieldToSearch);
		
		CcpTextDecorator ccpTextDecorator = new CcpTextDecorator(asString);
		
		String message = ccpTextDecorator.getMessage(this).content;
		 
		CcpJsonRepresentation put = this.put(fieldToPut, message);
		
		return put;
	}
	
	public CcpTextDecorator getAsTextDecorator(String field) {
		String asString = this.getAsString(field);
		CcpStringDecorator ccpStringDecorator = new CcpStringDecorator(asString);
		CcpTextDecorator text = ccpStringDecorator.text();
		return text;
	}
	
	public String getAsString(String field) {

		Object object = this.content.get(field);
		
		if(false == this.content.containsKey(field) || object == null ) {
			return ""; 
		}
		return ("" + object);
	}

	@SuppressWarnings("unchecked")
	public <T> T getOrDefault(String field, T defaultValue) {
		Object object = this.content.get(field);
		
		if(null == object) {
			return defaultValue;
		}
		
		if(defaultValue instanceof String) {
			return (T) ("" + object);
		}
		
		return (T)object;
	}
	
	public CcpJsonRepresentation getJsonPiece(Collection<String> fields) {
		String[] array = fields.toArray(new String[fields.size()]);
		CcpJsonRepresentation jsonPiece = this.getJsonPiece(array);
		return jsonPiece;
	}	
	
	public CcpJsonRepresentation getJsonPiece(String... fields) {
		
		Map<String, Object> subMap = new LinkedHashMap<>();
		
		for (String field : fields) {
			Object value = this.content.get(field);
			subMap.put(field, value);
		}
		
		return new CcpJsonRepresentation(subMap);
	}


	public String asUgglyJson() {
		CcpJsonHandler json = CcpDependencyInjection.getDependency(CcpJsonHandler.class);
		String json2 = json.toJson(this.content);
		return json2;
	}
	
	public String asPrettyJson() {
		CcpJsonHandler json = CcpDependencyInjection.getDependency(CcpJsonHandler.class);
		String asPrettyJson = json.asPrettyJson(this.content);
		return asPrettyJson;
	}
	
	
	public String toString() {
		CcpJsonHandler json = CcpDependencyInjection.getDependency(CcpJsonHandler.class);
		String _json = json.asPrettyJson(new TreeMap<>(this.content));
		return _json;
	}
	

	public Set<String> fieldSet(){
		Set<String> keySet = this.content.keySet();
		return keySet;
	}

	public CcpJsonRepresentation put(String field, CcpJsonRepresentation map) {
		CcpJsonRepresentation put = this.put(field, map.content);
		return put;
	}

	public CcpJsonRepresentation put(String field, Collection<CcpJsonRepresentation> list) {
		List<Map<String, Object>> collect = list.stream().map(x -> x.content).collect(Collectors.toList());
		CcpJsonRepresentation put = this.put(field, collect);
		return put;
	}
	
	public <T> T getTransformed(Function<CcpJsonRepresentation, T> transformer) {
		T execute = transformer.apply(this);
		return execute;
	}
	
	@SafeVarargs
	public final CcpJsonRepresentation getTransformedJson(Function<CcpJsonRepresentation, CcpJsonRepresentation>... transformers) {
	
		CcpJsonRepresentation transformedJson = this;
		
		for (Function<CcpJsonRepresentation, CcpJsonRepresentation> transformer : transformers) {
			transformedJson = transformer.apply(transformedJson);
		}
		return transformedJson;
	}
	
	@SuppressWarnings("unchecked")
	public CcpJsonRepresentation getTransformedJsonIfFoundTheField(String field, Function<CcpJsonRepresentation, CcpJsonRepresentation>... transformers) {

		if(containsAllFields(field) == false) {
			return this;
		}
		
		CcpJsonRepresentation transformedJson = this;
		
		for (Function<CcpJsonRepresentation, CcpJsonRepresentation> transformer : transformers) {
			transformedJson = transformer.apply(transformedJson);
		}
		
		return transformedJson;
	}
	
	
	public CcpJsonRepresentation addJsonTransformer(String field, Function<CcpJsonRepresentation, CcpJsonRepresentation> process) {
		CcpJsonRepresentation put = this.put(field, process);
		return put;
	}
	
	public CcpJsonRepresentation put(String field, Object value) {
		
		Map<String, Object> content = new LinkedHashMap<>();
		content.putAll(this.content);
		content.put(field, value);
		return new CcpJsonRepresentation(content);
	}  

	public CcpJsonRepresentation duplicateValueFromField(String fieldToCopy, String... fieldsToPaste) {
		boolean inexistentField = this.containsAllFields(fieldToCopy) == false;

		if (inexistentField) {
			return this;
		}
		
		CcpJsonRepresentation newMap = this;
		
		for (String fieldToPaste : fieldsToPaste) {
			Object value = this.get(fieldToCopy);
			newMap = newMap.put(fieldToPaste, value);
		}
		
		return newMap;
	}
	
	
	public CcpJsonRepresentation renameField(String oldField, String newField) {
		Map<String, Object> content = new HashMap<>();
		content.putAll(this.content);
		Object value = content.remove(oldField);
		if(value == null) {
			CcpJsonRepresentation json = new CcpJsonRepresentation(content);
			return json;
		}
		
		content.put(newField, value);
		CcpJsonRepresentation json = new CcpJsonRepresentation(content);
		return json;
		
	}
	
	public CcpJsonRepresentation removeField(String field) {
		Map<String, Object> content = this.getContent();
		Map<String, Object> copy = new HashMap<>(content);
		copy.remove(field);
		CcpJsonRepresentation json = new CcpJsonRepresentation(copy);
		return json;
	}
	
	public CcpJsonRepresentation removeFields(Collection<String> fields) {
		String[] array = fields.toArray(new String[fields.size()]);
		CcpJsonRepresentation removeFields = this.removeFields(array);
		return removeFields;
	}
	
	
	public CcpJsonRepresentation removeFields(String... fields) {
		CcpJsonRepresentation json = this;
		for (String field : fields) {
			json = json.removeField(field);
		}
		return json;
	}

	public Map<String, Object> getContent() {
		return this.content;
	}
	
	public CcpJsonRepresentation copy() {
		return new CcpJsonRepresentation(this.getContent());
	}

	
	public CcpJsonRepresentation getInnerJsonFromPath(String...paths) {
		try {
			Map<String, Object> map =  this.getValueFromPath(paths);
			CcpJsonRepresentation json = new CcpJsonRepresentation(map);
			return json;
		} catch (ClassCastException e) {
			CcpJsonRepresentation map =  this.getValueFromPath(paths);
			return map;
		}catch (JsonFieldNotFound e) {
			return CcpOtherConstants.EMPTY_JSON;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T>T getValueFromPath(T defaultValue, String... paths){
		CcpJsonRepresentation initial = this;
		for(int k = 0; k < paths.length -1 ; k++) {
			String path = paths[k];
			initial = initial.getInnerJson(path);
		}
		
		String lastPath = paths[paths.length - 1];
		
		boolean isNotPresent = initial.containsField(lastPath) == false;
		
		if(isNotPresent) {
			return defaultValue;
		}
		
		Object object = initial.get(lastPath);
		return (T) object;
	}
	
	@SuppressWarnings("unchecked")
	private <T>T getValueFromPath(String... paths){
		CcpJsonRepresentation initial = this;
		for(int k = 0; k < paths.length -1 ; k++) {
			String path = paths[k];
			initial = initial.getInnerJson(path);
		}
		
		String lastPath = paths[paths.length - 1];
		
		Object object = initial.get(lastPath);
		return (T) object;
	}
	
	@SuppressWarnings("unchecked")
	public CcpJsonRepresentation getInnerJson(String field) {
		Object object = this.content.get(field);
	
		
		if(object instanceof CcpJsonRepresentation) {
			return (CcpJsonRepresentation) object;
		}
		
		if(object instanceof String) {
			return new CcpJsonRepresentation("" + object);
		}
		

		if((object instanceof Map) == false) {
			return CcpOtherConstants.EMPTY_JSON;
		}

		CcpJsonRepresentation mapDecorator = new CcpJsonRepresentation((Map<String, Object>) object);
		return mapDecorator;
	}
	
	@SuppressWarnings("unchecked")
	public List<CcpJsonRepresentation> getAsJsonList(String field) {
		
		Object object = this.content.get(field);
		 
		if(object == null) {
			return new ArrayList<>();
		}   
		
		if(object instanceof Collection == false) {
			return new ArrayList<>();
		}
		
		Collection<Object> list = (Collection<Object>) object;
		
		List<CcpJsonRepresentation> collect = list.stream().map(obj -> new CcpJsonRepresentation((Map<String, Object>) obj))
				.collect(Collectors.toList());
		
		return collect;
	}

	public CcpCollectionDecorator getAsCollectionDecorator(String field){
		List<String> asStringList = this.getAsStringList(field);
		Object[] array = asStringList.toArray(new String[asStringList.size()]);
		CcpCollectionDecorator ccpCollectionDecorator = new CcpCollectionDecorator(array);
		return ccpCollectionDecorator;
	}
	
	public List<String> getAsStringList(String field){
		List<String> collect = this.getAsObjectList(field).stream()
				.filter(x -> x != null)
				.map(x -> x.toString()).collect(Collectors.toList());
		return collect;
	}
	
	public List<String> getAsStringList(String field, String alternativeField){
		List<String> asStringList = this.getAsStringList(field);
		boolean found = asStringList.isEmpty() == false;
		if(found) {
			return asStringList;
		}
		String asString = this.getAsString(alternativeField);
		return Arrays.asList(asString);
	}
	
	public List<Object> getAsObjectList(String field) {
		
		boolean isNotPresent = this.containsAllFields(field) == false;
		
		if(isNotPresent) {
			return new ArrayList<>();
		}
		
		Object object = this.content.get(field);
		
		if(object == null) {
			return new ArrayList<>();
		}
		
		if(object instanceof Collection<?> list) {
			return new ArrayList<Object>(list);
		}
		
		CcpJsonHandler jsonHandler = CcpDependencyInjection.getDependency(CcpJsonHandler.class);
		try {
			List<Object> fromJson = jsonHandler.fromJson(object.toString());
			return fromJson;
		} catch (Exception e) {
			return Arrays.asList(object.toString());
		}
	}
	
	
	public CcpJsonRepresentation putAll(Map<String, Object> map) {
		Map<String, Object> content2 = this.getContent();
		Map<String, Object> content = new LinkedHashMap<>(content2);
		content.putAll(map);
		CcpJsonRepresentation mapDecorator = new CcpJsonRepresentation(content);
		return mapDecorator;
	}

	public CcpJsonRepresentation putAll(CcpJsonRepresentation json) {
		CcpJsonRepresentation mapDecorator = this.putAll(json.content);
		return mapDecorator;
	}
	
	public boolean containsField(String field) {
		Object object = this.content.get(field);
		return object != null;
	}

	public boolean containsAllFields(Collection<String> fields) {
		String[] array = this.toArray(fields);
		boolean containsAllFields = this.containsAllFields(array);
		return containsAllFields;
	}

	private String[] toArray(Collection<String> fields) {
		int size = fields.size();
		String[] a = new String[size];
		String[] array = fields.toArray(a);
		return array;
	}	
	
	public boolean containsAllFields(String... fields) {
		boolean containsFields = this.containsFields(false, fields);
		return containsFields;
	}
	
	public boolean containsAnyFields(Collection<String> fields) {
		String[] array = this.toArray(fields);
		boolean containsAnyFields = this.containsAnyFields(array);
		return containsAnyFields;
	}

	public boolean containsAnyFields(String... fields) {
		boolean containsFields = this.containsFields(true, fields);
		return containsFields;
	}

	private boolean containsFields(boolean assertion, String... fields) {
		for (String field : fields) {
			boolean containsField = this.containsField(field);
			if(containsField == assertion) {
				return assertion;
			}
		}
		return assertion == false;
	}
	
	public Object get(String field) {
		Object object = this.content.get(field);
		boolean valueIsAbsent = object == null;
		if(valueIsAbsent) {
			throw new JsonFieldNotFound(field, this);
		}
		return object;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAsObject(String... fields) {
		for (String field : fields) {
			Object object = this.content.get(field);
			if(object == null) {
				continue;
			}
			return (T) object;
		}
		String message = "fields " + Arrays.asList(fields) + " not found in the json " + this;
		throw new RuntimeException(message);
	}
	
	public boolean isEmpty() {
		boolean empty = this.content.isEmpty();
		return empty;
	}
	

	public CcpJsonRepresentation addToList(String field, Object... values) {
		CcpJsonRepresentation result = this;
		for (Object value : values) {
			result = result.addToList(field, value);
		}
		return result;
	}
	
	public CcpJsonRepresentation addToList(String field, Object value) {
		List<Object> list = this.getAsObjectList(field);
		list = new ArrayList<>(list);
		list.add(value);
		CcpJsonRepresentation put = this.put(field, list);
		return put;
	}

	public CcpJsonRepresentation addToList(String field, CcpJsonRepresentation value) {
		List<Object> list = this.getAsObjectList(field);
		list = new ArrayList<>(list);
		list.add(value.content);
		CcpJsonRepresentation put = this.put(field, list);
		return put;
	}

	public CcpJsonRepresentation addToItem(String field, String subField, Object value) {
		CcpJsonRepresentation itemAsMap = this.getInnerJson(field);
		itemAsMap = itemAsMap.put(subField, value);
		
		CcpJsonRepresentation put = this.put(field, itemAsMap.content);
		return put;
	}
	
	public CcpJsonRepresentation addToItem(String field, String subField, CcpJsonRepresentation value) {
		CcpJsonRepresentation itemAsMap = this.getInnerJson(field);
		itemAsMap = itemAsMap.put(subField, value.content);
		
		CcpJsonRepresentation put = this.put(field, itemAsMap.content);
		return put;
	}

	public CcpJsonRepresentation whenHasField(String field, Function<CcpJsonRepresentation, CcpJsonRepresentation> process) {
		
		boolean hasNot = this.containsAllFields(field) == false;
		
		if(hasNot) {
			CcpJsonRepresentation response = new CcpJsonRepresentation(this.content);
			return response;
		}
		
		CcpJsonRepresentation execute = process.apply(this);
		
		return execute;
	}

	public CcpJsonRepresentation whenHasNotField(String field, Function<CcpJsonRepresentation, CcpJsonRepresentation> process) {
		
		boolean has = this.containsAllFields(field);
		
		if(has) {
			CcpJsonRepresentation response = new CcpJsonRepresentation(this.content);
			return response;
		}
		
		CcpJsonRepresentation execute = process.apply(this);
		
		return execute;
	}

	public CcpJsonRepresentation copyIfNotContains(String fieldToCopy, String fieldToPaste) {

		boolean containsAllFields = this.containsAllFields(fieldToPaste);
		
		if(containsAllFields) {
			return this;
		}
	
		CcpJsonRepresentation duplicateValueFromField = this.duplicateValueFromField(fieldToCopy, fieldToPaste);
		
		return duplicateValueFromField;
	}		
	
	public CcpJsonRepresentation putIfNotContains(String field, Object value) {

		boolean containsAllFields = this.containsAllFields(field);
		
		if(containsAllFields) {
			return this;
		}
		
		CcpJsonRepresentation put = this.put(field, value);
		return put;
	}

	public CcpCollectionDecorator getAsArrayMetadata(String field) {
		CcpCollectionDecorator cccpCollectionDecorator = new CcpCollectionDecorator(this, field);
		return cccpCollectionDecorator;
	}
	
	public ItIsTrueThatTheFollowingFields itIsTrueThatTheFollowingFields(String...fields) {
		return new ItIsTrueThatTheFollowingFields(this, fields);
	}
	
	public Set<String> getMissingFields(Collection<String> fields){
		Set<String> collect = fields.stream().filter(field -> this.getAsString(field).trim().isEmpty()).collect(Collectors.toSet());
		return collect;
	}
	
	public InputStream toInputStream() {
		String asUgglyJson = this.asUgglyJson();
		byte[] bytes = asUgglyJson.getBytes(StandardCharsets.UTF_8);
		InputStream stream = new ByteArrayInputStream(bytes);
		return stream;
	}
	
	public String getSha1Hash(CcpHashAlgorithm algorithm) {
		String asUgglyJson = this.asUgglyJson();
		String hash = new CcpStringDecorator(asUgglyJson).hash().asString(algorithm);
		return hash;
	}

	public int hashCode() {
		String hash2 = this.getSha1Hash(CcpHashAlgorithm.SHA1);
		int hashCode = hash2.hashCode();
		return hashCode;
	}
	
	public boolean equals(Object obj) {
		
		if(obj instanceof CcpJsonRepresentation other) {
			String hash = other.getSha1Hash(CcpHashAlgorithm.SHA1);
			String hash2 = this.getSha1Hash(CcpHashAlgorithm.SHA1);
			boolean equals = hash.equals(hash2);
			return equals;
		}
		
		return false;
	}
	
	public List<CcpTextDecorator> getAsTextDecoratorList(String field){
		List<String> asStringList = this.getAsStringList(field);
		List<CcpTextDecorator> collect = asStringList.stream().map(x -> new CcpTextDecorator(x)).collect(Collectors.toList());
		return collect;
	}
	
	public CcpJsonRepresentation putNewFieldHash(String oldField, String newField, CcpHashAlgorithm hashType) {
		String value = this.getAsString(oldField);
		CcpHashDecorator hash2 = new CcpStringDecorator(value).hash();
		String hash = hash2.asString(hashType);
		CcpJsonRepresentation put2 = this.put(oldField, hash);
		CcpJsonRepresentation put = put2.put(newField, value);
		return put;
	}
	
	public CcpJsonRepresentation putEmailHash(CcpHashAlgorithm hashType) {
		CcpJsonRepresentation putNewFieldHash = this.putNewFieldHash(CcpStringConstants.EMAIL.value, "originalEmail", hashType);
		return putNewFieldHash;
	}
	
	public CcpJsonRepresentation putHashPassword(String fieldName, CcpHashAlgorithm hashType) {
		String value = this.getAsString(fieldName);
		CcpHashDecorator hash2 = new CcpStringDecorator(value).hash();
		String hash = hash2.asString(hashType);
		CcpJsonRepresentation put2 = this.put(fieldName, hash);
		return put2;
	}
	
	public CcpJsonRepresentation putRandomToken(int tokenSize, String fieldName) {
		
		CcpStringDecorator csd = new CcpStringDecorator(CcpStringConstants.CHARACTERS_TO_GENERATE_TOKEN.value);
		CcpTextDecorator text = csd.text();
		CcpTextDecorator generateToken = text.generateToken(tokenSize);
		String token = generateToken.content;
		
		CcpJsonRepresentation put = this.put(fieldName, token);
		
		return put;
	}

	public CcpJsonRepresentation putRandomPassword(int tokenSize, String fieldName, String fieldHashName) {
		
		CcpJsonRepresentation transformed = this.putRandomToken(tokenSize, fieldName);
		
		String token = transformed.getAsString(fieldName);
		
		CcpPasswordHandler dependency = CcpDependencyInjection.getDependency(CcpPasswordHandler.class);
		
		String tokenHash = dependency.getHash(token);
		
		CcpJsonRepresentation put = transformed.put(fieldHashName, tokenHash);
		
		return put;

	}

	public CcpJsonRepresentation putPasswordHash(String fieldName) {
		CcpJsonRepresentation putPasswordHash = this.putPasswordHash(fieldName, fieldName);
		return putPasswordHash;
	}
	
	public CcpJsonRepresentation putPasswordHash(String fieldName, String fieldHashName) {
		
		String password = this.getAsString(fieldName);
		
		CcpPasswordHandler dependency = CcpDependencyInjection.getDependency(CcpPasswordHandler.class);
		
		String passwordHash = dependency.getHash(password);
		
		CcpJsonRepresentation put = this.put(fieldHashName, passwordHash);
		
		return put;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T removeFieldReturningValue(String field) {
		Object remove = this.content.remove(field);
		return (T)remove;
	}
}

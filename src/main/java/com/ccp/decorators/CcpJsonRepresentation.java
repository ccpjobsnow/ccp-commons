package com.ccp.decorators;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ccp.constantes.CcpConstants;
import com.ccp.dependency.injection.CcpDependencyInjection;
import com.ccp.especifications.json.CcpJsonHandler;
import com.ccp.validation.ItIsTrueThatTheFollowingFields;
 
public final class CcpJsonRepresentation {

	public final Map<String, Object> content;
	
	protected CcpJsonRepresentation() {
		this.content = new HashMap<>();
	}
	
	public static CcpJsonRepresentation getEmptyJson() {
		return new CcpJsonRepresentation();
	}
	
	public CcpJsonRepresentation(Object obj) {
		this(CcpDependencyInjection.getDependency(CcpJsonHandler.class).toJson(obj));
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
	
	public CcpJsonRepresentation(List<CcpJsonRepresentation> list, String key, String value) {
		CcpJsonRepresentation obj = CcpConstants.EMPTY_JSON;
		for (CcpJsonRepresentation md : list) {
			String _key = md.getAsString(key);
			Object _value = md.get(value);
			obj = obj.put(_key, _value);
		}
		this.content = obj.content;
	}
	
	
	public String getHash(String algorithm) {
		String asJson = this.asUgglyJson();
		CcpStringDecorator ccpStringDecorator = new CcpStringDecorator(asJson);
		CcpHashDecorator hash = ccpStringDecorator.hash();
		String asString = hash.asString(algorithm);
		return asString;
	}
	
	public CcpJsonRepresentation(Throwable e) {
		this(getErrorDetails(e));
	}

	public CcpJsonRepresentation(String json) {
		this(getMap(json));
	}

	static Map<String, Object> getMap(String _json) {
		CcpJsonHandler json = CcpDependencyInjection.getDependency(CcpJsonHandler.class);
		try {
			Map<String, Object> fromJson = json.fromJson(_json);
			return fromJson;
			
		} catch (Exception e) {
			throw new RuntimeException(json + " está inválido", e);
		}
	}
	
	public CcpJsonRepresentation(CcpJsonRepresentation md) {
		this(md.content);
	}
	
	public CcpJsonRepresentation(Map<String, Object> content) {
		this.content = Collections.unmodifiableMap(content);
	}

	private static CcpJsonRepresentation getErrorDetails(Throwable e) {

		CcpJsonRepresentation jr = CcpConstants.EMPTY_JSON;

		if (e == null) {
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
			String key = fileName.replace(".java", "") + "." + methodName + ":" + lineNumber+ "\n";
			stackTrace.add(key);
		}
		
		CcpJsonRepresentation causeDetails = getErrorDetails(cause);

		jr = jr.put("type", e.getClass().getName()).put("stackTrace", stackTrace.toString()).put("message", message).put("cause", causeDetails);
		return jr;
	}
	
	@SuppressWarnings("deprecation")
	public Long getAsLongNumber(String property) {
		
		Object object = this.content.get(property);
		try {
			if(object instanceof Date) {
				return ((Date)object).getTime();
			}
			
			return Double.valueOf("" + object).longValue();
		} catch (Exception e) {
			if(object instanceof String) {
				try {
					Date d = new Date("" + object);
					long time = d.getTime();
					return time;
				} catch (IllegalArgumentException ex) {
					// tem nada pra fazer aqui nao
				}
			}
			
			return null;
		}
		
	}

	public Integer getAsIntegerNumber(String property) {
		
		try {
			return Double.valueOf("" + this.content.get(property)).intValue();
		} catch (Exception e) {
			return null;
		}
		
	}
	

	public boolean getAsBoolean(String property) {
		
		try {
			String asString = this.getAsString(property);
			return Boolean.valueOf(asString.toLowerCase());
		} catch (Exception e) {
			return false;
		}
	}
	
	
	public int getAsFlag(String property) {
		boolean asBoolean = this.getAsBoolean(property);
	
		if(Boolean.TRUE.equals(asBoolean)) {
			return 1;
		}
		
		return 0;
	}

	public Double getAsDoubleNumber(String property) {
		
		try {
			return Double.valueOf("" + this.content.get(property));
		} catch (Exception e) {
			return null;
		}
	}
	
	
	public Date getAsData(String property) {
		
		try {
			String asString = this.getAsString(property);
			Double double1 = Double.valueOf(asString);
			long longValue = double1.longValue();
			return new Date(longValue);
		} catch (Exception e) {
			return null;
		}
	}
	
	public CcpJsonRepresentation putFilledTemplate(String fieldToSearch, String fieldToPut) {
		
		String asString = this.getAsString(fieldToSearch);
		
		CcpTextDecorator ccpTextDecorator = new CcpTextDecorator(asString);
		
		String message = ccpTextDecorator.getMessage(this);
		 
		CcpJsonRepresentation put = this.put(fieldToPut, message);
		
		return put;
	}
	
	public String getAsString(String property) {

		if(false == this.content.containsKey(property) || this.content.get(property) == null ) {
			return ""; 
		}
		return ("" + this.content.get(property));
	}

	public String getAsBoolean(String key, String trueValue, String falseValue, String nullValue) {
		
		if(this.content.containsKey(key) == false) {
			return nullValue;
		}
		if(this.getAsBoolean(key) == false) {
			return falseValue;
		}
		return trueValue;
	}
	
	public String getDecimalFormat(String key, String format, String defaultValue) {
		Double asDoubleNumber = this.getAsDoubleNumber(key);
		
		if(asDoubleNumber == null) {
			return defaultValue;
		}
		
		String format2 = new DecimalFormat(format).format(asDoubleNumber);
		
		return format2;
	}

	@SuppressWarnings("unchecked")
	public <T> T getOrDefault(String name, T valorPadrao) {
		Object object = this.content.get(name);
		
		if(null == object) {
			return valorPadrao;
		}
		
		if(valorPadrao instanceof String) {
			return (T) ("" + object);
		}
		
		return (T)object;
	}
	
	
	public Double getAsNumber(String key) {
		if(false == this.content.containsKey(key)) {
			return 0d;
		}
		
		
		Double asDoubleNumber = this.getAsDoubleNumber(key);
		return asDoubleNumber == null ? 0d : asDoubleNumber;
	}
	
	public CcpJsonRepresentation getJsonPiece(String...keys) {
		
		Map<String, Object> subMap = new LinkedHashMap<>();
		
		for (String key : keys) {
			Object value = this.content.get(key);
			subMap.put(key, value);
		}
		
		return new CcpJsonRepresentation(subMap);
	}


	public String asUgglyJson() {
		CcpJsonHandler json = CcpDependencyInjection.getDependency(CcpJsonHandler.class);
		return json.toJson(this.content);
	}
	
	public String asPrettyJson() {
		CcpJsonHandler json = CcpDependencyInjection.getDependency(CcpJsonHandler.class);
		return json.asPrettyJson(this.content);
	}
	
	
	public String toString() {
		CcpJsonHandler json = CcpDependencyInjection.getDependency(CcpJsonHandler.class);

		String _json = json.asPrettyJson(new TreeMap<>(this.content));
		return _json;
	}
	

	public Set<String> keySet(){
		return this.content.keySet();
	}

	public CcpJsonRepresentation put(String key, CcpJsonRepresentation map) {
		CcpJsonRepresentation put = this.put(key, map.content);
		return put;
	}

	public CcpJsonRepresentation putSubKey(String key, String subKey, CcpJsonRepresentation value) {
		CcpJsonRepresentation internalMap = this.getInnerJson(key);
		internalMap = internalMap.put(subKey, value);
		CcpJsonRepresentation put = this.put(key, internalMap);
		return put;
	}

	
	public CcpJsonRepresentation putSubKey(String key, String subKey, Object value) {
		CcpJsonRepresentation internalMap = this.getInnerJson(key);
		internalMap = internalMap.put(subKey, value);
		CcpJsonRepresentation put = this.put(key, internalMap);
		return put;
	}
	public CcpJsonRepresentation putSubKeyAsString(String key, String subKey, Object value) {
		CcpJsonRepresentation internalMap = this.getInnerJson(key);
		internalMap = internalMap.put(subKey, value);
		CcpJsonRepresentation put = this.put(key, internalMap.asUgglyJson());
		return put;
	}

	public CcpJsonRepresentation put(String key, Collection<CcpJsonRepresentation> list) {
		List<Map<String, Object>> collect = list.stream().map(x -> x.content).collect(Collectors.toList());
		CcpJsonRepresentation put = this.put(key, collect);
		return put;
	}
	
	public CcpJsonRepresentation getTransformed(Function<CcpJsonRepresentation, CcpJsonRepresentation> transformer) {
		CcpJsonRepresentation execute = transformer.apply(this);
		return execute;
	}
	
	public CcpJsonRepresentation put(String key, Function<CcpJsonRepresentation, CcpJsonRepresentation> process) {
		CcpJsonRepresentation put = this.put(key, (Object)process);
		return put;
	}
	
	public <T,R> CcpJsonRepresentation putTransformedValue(String oldKey, String newKey, Function<T,R> function) {
		T  oldValue = this.getAsObject(oldKey);
		R newValue = function.apply(oldValue);
		CcpJsonRepresentation put = this.put(newKey, newValue);
		return put;
	}
	
	
	public CcpJsonRepresentation put(String key, Object value) {
		
		Map<String, Object> content = new LinkedHashMap<>();
		content.putAll(this.content);
		content.put(key, value);
		return new CcpJsonRepresentation(content);
	}  

	public CcpJsonRepresentation duplicateValueFromKey(String keyToCopy, String keyToPaste) {
		Object value = this.get(keyToCopy);
		CcpJsonRepresentation newMap = this.put(keyToPaste, value);
		return newMap;
	}
	
	
	public CcpJsonRepresentation renameKey(String oldKey, String newKey) {
		Map<String, Object> content = new HashMap<>();
		content.putAll(this.content);
		Object value = content.remove(oldKey);
		CcpJsonRepresentation ccpMapDecorator = new CcpJsonRepresentation(content);
		if(value == null) {
			return ccpMapDecorator;
		}
		
		content.put(newKey, value);
		CcpJsonRepresentation mapDecorator = ccpMapDecorator;
		return mapDecorator;
		
	}
	
	public CcpJsonRepresentation removeKey(String key) {
		Map<String, Object> copy = new HashMap<>(this.getContent());
		copy.remove(key);
		CcpJsonRepresentation mapDecorator = new CcpJsonRepresentation(copy);
		return mapDecorator;
	}
	
	public CcpJsonRepresentation removeKeys(String... keys) {
		CcpJsonRepresentation modifiedCopy = this;
		for (String key : keys) {
			modifiedCopy = modifiedCopy.removeKey(key);
		}
		return modifiedCopy;
	}

	public CcpJsonRepresentation cloneKey(String key, String newKey) {
		Map<String, Object> copy = new HashMap<>(this.getContent());
		Object value = copy.get(key);
		CcpJsonRepresentation mapDecorator = new CcpJsonRepresentation(copy).put(newKey, value);
		return mapDecorator;
	}

	public Map<String, Object> getContent() {
		return this.content;
	}
	
	public CcpJsonRepresentation copy() {
		return new CcpJsonRepresentation(this.getContent());
	}

	@SuppressWarnings("unchecked")
	public CcpJsonRepresentation getInnerJson(String key) {
		Object object = this.content.get(key);
	
		
		if(object instanceof CcpJsonRepresentation) {
			return (CcpJsonRepresentation) object;
		}
		
		if(object instanceof String) {
			return new CcpJsonRepresentation("" + object);
		}
		

		if((object instanceof Map) == false) {
			return CcpConstants.EMPTY_JSON;
		}

		CcpJsonRepresentation mapDecorator = new CcpJsonRepresentation((Map<String, Object>) object);
		return mapDecorator;
	}
	
	@SuppressWarnings("unchecked")
	public List<CcpJsonRepresentation> getAsJsonList(String name) {
		
		Object object = this.content.get(name);
		 
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

	public CcpCollectionDecorator getAsCollectionDecorator(String key){
		List<String> asStringList = this.getAsStringList(key);
		CcpCollectionDecorator ccpCollectionDecorator = new CcpCollectionDecorator(asStringList);
		return ccpCollectionDecorator;
	}
	
	public List<String> getAsStringList(String key){
		List<String> collect = this.getAsObjectList(key).stream()
				.filter(x -> x != null)
				.map(x -> x.toString()).collect(Collectors.toList());
		return collect;
	}
	
	public List<String> getAsStringList(String key, String alternativeKey){
		List<String> asStringList = this.getAsStringList(key);
		boolean found = asStringList.isEmpty() == false;
		if(found) {
			return asStringList;
		}
		String asString = this.getAsString(alternativeKey);
		return Arrays.asList(asString);
	}
	public List<Object> getAsObjectList(String key) {
		
		boolean isNotPresent = this.containsAllKeys(key) == false;
		if(isNotPresent) {
			return new ArrayList<>();
		}
		
		Object object = this.content.get(key);
		
		if(object instanceof String) {
			CcpJsonHandler jsonHandler = CcpDependencyInjection.getDependency(CcpJsonHandler.class);
			try {
				List<Object> fromJson = jsonHandler.fromJson(object.toString());
				return fromJson;
			} catch (Exception e) {
			}
			List<Object> lista = new ArrayList<>();
			
			String[] split = object.toString().split(",");
			
			for (String string : split) {
				lista.add(string);
			}
			
			return lista;
		}
		
		if(object instanceof Collection<?> list) {
			return new ArrayList<Object>(list);
		}
		
		throw new WrongType(key, Collection.class, object.getClass());
		
	}
	
	@SuppressWarnings("serial")
	public static class WrongType extends RuntimeException{
		private WrongType(String key, Class<?> expectedType, Class<?> actuallyType) {
			super(String.format("The key '%s' must be of type '%s', but is of the type '%s'", key, expectedType.getName(), actuallyType.getName()));
		}
	}
	
	public CcpJsonRepresentation putAll(Map<String, Object> map) {
		Map<String, Object> content = new HashMap<>(this.getContent());
		content.putAll(map);
		CcpJsonRepresentation mapDecorator = new CcpJsonRepresentation(content);
		return mapDecorator;
	}

	public CcpJsonRepresentation putAll(CcpJsonRepresentation md) {
		CcpJsonRepresentation mapDecorator = this.putAll(md.content);
		return mapDecorator;
	}
	
	public boolean containsKey(String key) {
		return this.content.containsKey(key);
	}

	public boolean containsAllKeys(String... keys) {
		boolean containsKeys = this.containsKeys(false, keys);
		return containsKeys;
	}

	public boolean containsAnyKeys(String... keys) {
		boolean containsKeys = this.containsKeys(true, keys);
		return containsKeys;
	}

	private boolean containsKeys(boolean assertion,String... keys) {
		for (String key : keys) {
			boolean containsKey = this.containsKey(key);
			if(containsKey == assertion) {
				return assertion;
			}
		}
		return assertion == false;
	}
	
	public Object get(String key) {
		Object object = this.content.get(key);
		return object;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAsObject(String key) {
		return (T) this.content.get(key);
	}
	
	public boolean isEmpty() {
		boolean empty = this.content.isEmpty();
		return empty;
	}
	

	public CcpJsonRepresentation addToList(String key, Object... values) {
		CcpJsonRepresentation result = this;
		for (Object value : values) {
			result = result.addToList(key, value);
		}
		return result;
	}
	
	public CcpJsonRepresentation addToList(String key, Object value) {
		List<Object> list = this.getAsObject(key);
		
		if(list == null) {
			list = new ArrayList<>();
		}
		
		list = new ArrayList<>(list);
		list.add(value);
		CcpJsonRepresentation put = this.put(key, list);
		return put;
	}

	public CcpJsonRepresentation addToList(String key, CcpJsonRepresentation value) {
		List<Object> list = this.getAsObject(key);
		
		if(list == null) {
			list = new ArrayList<>();
		}
		
		list = new ArrayList<>(list);
		list.add(value.content);
		CcpJsonRepresentation put = this.put(key, list);
		return put;
	}

	public CcpJsonRepresentation addToItem(String key, String subKey, Object value) {
		CcpJsonRepresentation itemAsMap = this.getInnerJson(key);
		itemAsMap = itemAsMap.put(subKey, value);
		
		CcpJsonRepresentation put = this.put(key, itemAsMap.content);
		return put;
	}
	
	public <T> T get(Function<CcpJsonRepresentation, T> transformer) {
		T transform = transformer.apply(this);
		return transform;
	}
	public String getAsString(String key, String completeWith, int maxLength) {
		
		String value = this.getAsString(key);
		
		if(value.length() <= maxLength) {
			return value;
		}
		
		String str = value.substring(0, maxLength) + completeWith;
	
		return str;
	}

	public String getDataFormatada(String key, String format) {
		Long asLongNumber = this.getAsLongNumber(key);
		if(asLongNumber == null) {
			return "";
		}
		String formattedDateTime = new CcpTimeDecorator(asLongNumber).getFormattedDateTime(format);
		return formattedDateTime;
	}
	

	public CcpJsonRepresentation whenHasKey(String key, Function<CcpJsonRepresentation, CcpJsonRepresentation> process) {
		
		boolean hasNot = this.containsAllKeys(key) == false;
		
		if(hasNot) {
			CcpJsonRepresentation response = new CcpJsonRepresentation(this);
			return response;
		}
		
		CcpJsonRepresentation execute = process.apply(this);
		
		return execute;
	}

	public CcpJsonRepresentation whenHasNotKey(String key, Function<CcpJsonRepresentation, CcpJsonRepresentation> process) {
		
		boolean has = this.containsAllKeys(key);
		
		if(has) {
			CcpJsonRepresentation response = new CcpJsonRepresentation(this);
			return response;
		}
		
		CcpJsonRepresentation execute = process.apply(this);
		
		return execute;
	}

	public CcpJsonRepresentation putIfNotContains(String key, Object value) {

		boolean containsAllKeys = this.containsAllKeys(key);
		
		if(containsAllKeys) {
			return this;
		}
		
		CcpJsonRepresentation put = this.put(key, value);
		return put;
	}

	public CcpStringDecorator getAsMetadata(String key) {
		CcpStringDecorator ccpValueDecorator = new CcpStringDecorator(this, key);
		return ccpValueDecorator;
	}
	
	public CcpCollectionDecorator getAsArrayMetadata(String key) {
		CcpCollectionDecorator cccpCollectionDecorator = new CcpCollectionDecorator(this, key);
		return cccpCollectionDecorator;
	}
	
	public ItIsTrueThatTheFollowingFields itIsTrueThatTheFollowingFields(String...fields) {
		return new ItIsTrueThatTheFollowingFields(this, fields);
	}
	
}

package com.ccp.decorators;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ccp.constantes.CcpConstants;
import com.google.gson.GsonBuilder;

public class CcpMapDecorator {
	public final Map<String, Object> content;
	
	public CcpMapDecorator() {
		this.content = new HashMap<>();
	}
	
	public CcpMapDecorator(List<CcpMapDecorator> list, String key, String value) {
		CcpMapDecorator obj = new CcpMapDecorator();
		for (CcpMapDecorator md : list) {
			String _key = md.getAsString(key);
			Object _value = md.get(value);
			obj = obj.put(_key, _value);
		}
		this.content = obj.content;
	}
	
	public CcpMapDecorator(Throwable e) {
		this(getErrorDetails(e));
	}

	public CcpMapDecorator(String json) {
		this(getMap(json));
	}
	@SuppressWarnings("unchecked")
	static Map<String, Object> getMap(String json) {
		try {
			Map<String, Object> fromJson = CcpConstants.gson.fromJson(json, Map.class);
			return fromJson;
			
		} catch (Exception e) {
			throw new RuntimeException(json + " está inválido", e);
		}
	}
	
	public CcpMapDecorator(CcpMapDecorator md) {
		this(md.content);
	}
	
	public CcpMapDecorator(Map<String, Object> content) {
		this.content = Collections.unmodifiableMap(content);
	}

	//ThrowableDecorator getErrorDetails()
	private static CcpMapDecorator getErrorDetails(Throwable e) {

		CcpMapDecorator jr = new CcpMapDecorator();

		if (e == null) {
			return jr;
		}

		Throwable cause = e.getCause();
		String message = e.getMessage();
		StackTraceElement[] stackTrace = e.getStackTrace();
		CcpMapDecorator causeDetails = getErrorDetails(cause);

		jr = jr.put("message", message).put("type", e.getClass().getName()).put("stackTrace", stackTrace).put("cause", causeDetails);
		return jr;
	}
	
	@SuppressWarnings("deprecation")
	public Long getAsLongNumber(String property) {
		
		Object object = this.content.get(property);
		try {
			if(object instanceof Date) {
				return ((Date)object).getTime();
			}
			
			return new Double ("" + object).longValue();
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
	
	public CcpMapDecorator getSubMap(String...keys) {
		
		Map<String, Object> subMap = new LinkedHashMap<>();
		
		for (String key : keys) {
			Object value = this.content.get(key);
			subMap.put(key, value);
		}
		
		return new CcpMapDecorator(subMap);
	}


	public String asJson() {
		return CcpConstants.gson.toJson(this.content);
	}
	
	public String asPrettyJson() {
		return new GsonBuilder().setPrettyPrinting().create().toJson(this.content);
	}
	
	@Override
	public String toString() {

		String json = new GsonBuilder().setPrettyPrinting().create().toJson(this.content);
		return json;
	}
	

	public Set<String> keySet(){
		return this.content.keySet();
	}

	public CcpMapDecorator put(String key, CcpMapDecorator map) {
		CcpMapDecorator put = this.put(key, map.content);
		return put;
	}
	
	public CcpMapDecorator putSubKey(String key, String subKey, Object value) {
		CcpMapDecorator internalMap = this.getInternalMap(key);
		internalMap = internalMap.put(subKey, value);
		CcpMapDecorator put = this.put(key, internalMap);
		return put;
	}
	public CcpMapDecorator putSubKeyAsString(String key, String subKey, Object value) {
		CcpMapDecorator internalMap = this.getInternalMap(key);
		internalMap = internalMap.put(subKey, value);
		CcpMapDecorator put = this.put(key, internalMap.asJson());
		return put;
	}

	public CcpMapDecorator put(String key, Collection<CcpMapDecorator> list) {
		List<Map<String, Object>> collect = list.stream().map(x -> x.content).collect(Collectors.toList());
		CcpMapDecorator put = this.put(key, collect);
		return put;
	}
	
	public CcpMapDecorator put(String key, Object value) {
		
		Map<String, Object> content = new LinkedHashMap<>();
		content.putAll(this.content);
		content.put(key, value);
		return new CcpMapDecorator(content);
	}  

	public CcpMapDecorator copyKeyValueToKeyPaste(String keyToCopy, String keyToPaste) {
		Object value = this.get(keyToCopy);
		CcpMapDecorator newMap = this.put(keyToPaste, value);
		return newMap;
	}
	
	
	public CcpMapDecorator renameKey(String oldKey, String newKey) {
		Map<String, Object> content = new HashMap<>();
		content.putAll(this.content);
		Object value = content.remove(oldKey);
		content.put(newKey, value);
		CcpMapDecorator mapDecorator = new CcpMapDecorator(content);
		return mapDecorator;
		
	}
	
	public CcpMapDecorator removeKey(String key) {
		Map<String, Object> copy = new HashMap<>(this.getContent());
		copy.remove(key);
		CcpMapDecorator mapDecorator = new CcpMapDecorator(copy);
		return mapDecorator;
	}
	
	public CcpMapDecorator removeKeys(String... keys) {
		CcpMapDecorator modifiedCopy = this;
		for (String key : keys) {
			modifiedCopy = modifiedCopy.removeKey(key);
		}
		return modifiedCopy;
	}

	public CcpMapDecorator cloneKey(String key, String newKey) {
		Map<String, Object> copy = new HashMap<>(this.getContent());
		Object value = copy.get(key);
		CcpMapDecorator mapDecorator = new CcpMapDecorator(copy).put(newKey, value);
		return mapDecorator;
	}

	public Map<String, Object> getContent() {
		return this.content;
	}
	
	public CcpMapDecorator copy() {
		return new CcpMapDecorator(this.getContent());
	}

	@SuppressWarnings("unchecked")
	public CcpMapDecorator getInternalMap(String key) {
		Object object = this.content.get(key);
	
		
		if(object instanceof CcpMapDecorator) {
			return (CcpMapDecorator) object;
		}
		
		if((object instanceof Map) == false) {
			return new CcpMapDecorator();
		}

		if(object instanceof String) {
			return new CcpMapDecorator("" + object);
		}
		
		CcpMapDecorator mapDecorator = new CcpMapDecorator((Map<String, Object>) object);
		return mapDecorator;
	}
	
	@SuppressWarnings("unchecked")
	public List<CcpMapDecorator> getAsMapList(String name) {
		
		Object object = this.content.get(name);
		 
		if(object == null) {
			return new ArrayList<>();
		}   
		
		if(object instanceof Collection == false) {
			return new ArrayList<>();
		}
		
		Collection<Object> list = (Collection<Object>) object;
		
		List<CcpMapDecorator> collect = list.stream().map(obj -> new CcpMapDecorator((Map<String, Object>) obj))
				.collect(Collectors.toList());
		
		return collect;
	}

	@SuppressWarnings("unchecked")
	public List<Object> getAsList(String name) {
		
		Object object = this.content.get(name);
		
		if(object instanceof String) {
			List<Object> lista = new ArrayList<>();
			
			String[] split = object.toString().split(",");
			
			for (String string : split) {
				lista.add(string);
			}
			
			return lista;
		}
		
		Collection<Object> list = (Collection<Object>) object;

		if(list == null) {
			return new ArrayList<>();
		}
		
		return new ArrayList<>(list);
	}
	
	public CcpMapDecorator putAll(Map<String, Object> map) {
		Map<String, Object> content = new HashMap<>(this.getContent());
		content.putAll(map);
		CcpMapDecorator mapDecorator = new CcpMapDecorator(content);
		return mapDecorator;
	}

	public CcpMapDecorator putAll(CcpMapDecorator md) {
		CcpMapDecorator mapDecorator = this.putAll(md.content);
		return mapDecorator;
	}
	
	public boolean containsKey(String key) {
		return this.content.containsKey(key);
	}

	public boolean containsAllKeys(String... keys) {
		for (String key : keys) {
			boolean containsKey = this.containsKey(key);
			if(containsKey == false) {
				return false;
			}
		}
		return true;
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
	
	public CcpMapDecorator addToList(String key, Object value) {
		List<Object> list = this.getAsObject(key);
		
		if(list == null) {
			list = new ArrayList<>();
		}
		
		list = new ArrayList<>(list);
		list.add(value);
		CcpMapDecorator put = this.put(key, list);
		return put;
	}

	public CcpMapDecorator addToList(String key, CcpMapDecorator value) {
		List<Object> list = this.getAsObject(key);
		
		if(list == null) {
			list = new ArrayList<>();
		}
		
		list = new ArrayList<>(list);
		list.add(value.content);
		CcpMapDecorator put = this.put(key, list);
		return put;
	}

	public CcpMapDecorator addToItem(String key, String subKey, Object value) {
		CcpMapDecorator itemAsMap = this.getInternalMap(key);
		itemAsMap = itemAsMap.put(subKey, value);
		
		CcpMapDecorator put = this.put(key, itemAsMap.content);
		return put;
	}
	
	public <T> T get(Transformer<T> transformer) {
		T transform = transformer.transform(this);
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
	
	public static interface Transformer<T>{
		T transform(CcpMapDecorator md);
	}
}

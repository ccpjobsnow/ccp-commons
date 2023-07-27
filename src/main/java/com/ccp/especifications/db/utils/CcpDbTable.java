package com.ccp.especifications.db.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.decorators.CcpStringDecorator;
import com.ccp.especifications.db.crud.CcpDbCrud;
import com.ccp.exceptions.commons.CcpFlow;
import com.ccp.exceptions.db.CcpRecordNotFound;
import com.ccp.process.CcpProcess;

public interface CcpDbTable {

	
	String name();
	
	String getId(CcpMapDecorator values);

	default String getId(CcpMapDecorator values,TimeOption timeOptioption, CcpDbTableField...fields) {

		Long time = values.getOrDefault("_time", System.currentTimeMillis());	
		
		String formattedCurrentDate = timeOptioption.getFormattedCurrentDate(time);
		
		if(fields.length == 0) {
			
			if(TimeOption.none != timeOptioption) {
				return formattedCurrentDate;
			}
			
			return UUID.randomUUID().toString();
		}
		
		List<CcpDbTableField> missingKeys = Arrays.asList(fields).stream().filter(key -> key.isPrimaryKey()).filter(key -> getValue(key, values).isEmpty()).collect(Collectors.toList());
		
		if(missingKeys.isEmpty() == false) {
			throw new CcpFlow(values, 500, "The following keys are missing to compose an id: " + missingKeys +" for entity " + this.name() + ". Current values: " + values, null);
		}
		
		
		List<String> onlyPrimaryKeys = Arrays.asList(fields).stream().filter(key -> key.isPrimaryKey()).map(key -> this.getValue(key, values)).collect(Collectors.toList());

		if(onlyPrimaryKeys.isEmpty()) {
			String hash = new CcpStringDecorator(formattedCurrentDate).hash().asString("SHA1");
			return hash;
		}
		
		Collections.sort(onlyPrimaryKeys);
		onlyPrimaryKeys = new ArrayList<>(onlyPrimaryKeys);

		if(formattedCurrentDate.trim().isEmpty() == false) {
			onlyPrimaryKeys.add(0, formattedCurrentDate);
		}
		
		String replace = onlyPrimaryKeys.toString().replace("[", "").replace("]", "");
		String hash = new CcpStringDecorator(replace).hash().asString("SHA1");
		return hash;
	}


	default String getValue(CcpDbTableField key, CcpMapDecorator values) {
		if(values.get(key.name()) instanceof Collection<?>) {
			Collection<?> col = values.getAsObject(key.name());
			ArrayList<?> list = new ArrayList<>(col);
			list.sort((a, b) -> ("" + a).compareTo("" + b));
			return list.toString();
		}
		
		
		return values.getAsString(key.name()).trim();
	}
	public static enum TimeOption{
		none{
			@Override
			public String getFormattedCurrentDate(Long time) {
				return "";
			}
		}
		,ddMMyyyy
		,ddMMyyyyHH
		,ddMMyyyyHHmm
		,ddMMyyyyHHmmss
		,ddMMyyyyHHmmssSSS
		;

		public String getFormattedCurrentDate(Long date) {
			Date d = new Date();
			d.setTime(date);
			String format = new SimpleDateFormat(this.name()).format(d);
			return format + "_";
		}
	} 

	default CcpMapDecorator get(CcpMapDecorator data, CcpProcess ifNotFound) {
		try {
			CcpDbCrud crud = this.getCrud();
			CcpMapDecorator oneById = crud.getOneById(this, data);
			return oneById;
			
		} catch (CcpRecordNotFound e) {
			CcpMapDecorator execute = ifNotFound.execute(data);
			return execute;
		}
	}

	default CcpMapDecorator get(CcpMapDecorator data) {
		CcpMapDecorator md = this.get(data, x -> {throw new CcpFlow(x, 404);});
		return md;
	}
	
	default boolean exists(CcpMapDecorator data) {
		boolean exists = this.getCrud().exists(this, data);
		return exists;
	}
	
	CcpMapDecorator getOnlyExistingFields(CcpMapDecorator values) ;
	
	default CcpMapDecorator save(CcpMapDecorator values) {
		CcpMapDecorator onlyExistingFields = this.getOnlyExistingFields(values);
		CcpDbCrud crud = this.getCrud();

		boolean updated = crud.updateOrSave(this, onlyExistingFields);

		this.saveAuditory(onlyExistingFields, updated);
		
		
		return onlyExistingFields;
	}
	
	void saveAuditory(CcpMapDecorator values, boolean updated)
	;

	CcpDbCrud getCrud();
	
	default CcpMapDecorator remove(CcpMapDecorator values) {
		CcpDbCrud crud = this.getCrud();
		CcpMapDecorator remove = crud.remove(this, values);
		return remove;
	}
	
	default List<CcpMapDecorator> getManyByIds(CcpMapDecorator... values){
		String[] ids = new String[values.length];
		
		int k = 0;
		
		for (CcpMapDecorator value : values) {
			String id = this.getId(value);
			ids[k++] = id;
		}
		CcpDbCrud crud = this.getCrud();
		List<CcpMapDecorator> manyByIds = crud.getManyByIds(this, ids);
	
		k = 0;
		List<CcpMapDecorator> response = new ArrayList<>();
		for (CcpMapDecorator value : values) {
			CcpMapDecorator md = manyByIds.get(k++);
			md = md.put("_id", value);
			response.add(md);
		}
		return response;
	}
	
	default List<CcpMapDecorator> getManyByIds(List< CcpMapDecorator> values){
		CcpMapDecorator[] array = values.toArray(new CcpMapDecorator[values.size()]);
		return this.getManyByIds(array);
	}	
}

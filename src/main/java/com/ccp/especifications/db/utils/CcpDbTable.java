package com.ccp.especifications.db.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	
	default String getId(CcpMapDecorator values,TimeOption timeOptioption, CcpDbTableField...fields) {
		
		String formattedCurrentDate = timeOptioption.getFormattedCurrentDate();
		
		if(fields.length == 0) {
			
			if(TimeOption.none != timeOptioption) {
				return formattedCurrentDate;
			}
			
			return UUID.randomUUID().toString();
		}
		
		List<CcpDbTableField> missingKeys = Arrays.asList(fields).stream().filter(key -> key.isPrimaryKey()).filter(key -> values.getAsString(key.name()).trim().isEmpty()).collect(Collectors.toList());
		
		if(missingKeys.isEmpty() == false) {
			throw new CcpFlow(values, 500, "The following keys are missing to compose an id: " + missingKeys + ". Current values: " + values, null);
		}
		
		
		List<String> onlyPrimaryKeys = Arrays.asList(fields).stream().filter(key -> key.isPrimaryKey()).map(key -> values.getAsString(key.name()).trim()).collect(Collectors.toList());

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
	public static enum TimeOption{
		none{
			@Override
			String getFormattedCurrentDate() {
				return "";
			}
		}
		,ddMMyyyy
		,ddMMyyyyHH
		,ddMMyyyyHHmm
		,ddMMyyyyHHmmss
		,ddMMyyyyHHmmssSSS
		;
		String getFormattedCurrentDate() {
			return getFormattedCurrentDate(System.currentTimeMillis());
		}

		public String getFormattedCurrentDate(Long date) {
			Date d = new Date();
			d.setTime(date);
			String format = new SimpleDateFormat(this.name()).format(d);
			return format + "_";
		}
	} 

	default CcpMapDecorator get(CcpMapDecorator data, CcpProcess ifNotFound) {
		try {
			String id = this.getId(data, this.getTimeOption(), this.getFields());
			CcpMapDecorator oneById = this.getCrud().getOneById(this, id);
			return oneById;
			
		} catch (CcpRecordNotFound e) {
			CcpMapDecorator execute = ifNotFound.execute(data);
			return execute;
		}
	}

	default CcpMapDecorator get(CcpMapDecorator data) {
		CcpMapDecorator md = this.get(data, x -> x);
		return md;
	}
	
	default boolean exists(CcpMapDecorator data) {
		String id = this.getId(data, this.getTimeOption(), this.getFields());
		boolean exists = this.getCrud().exists(this, id);
		return exists;
	}
	
	default CcpMapDecorator getOnlyExistingFields(CcpMapDecorator values) {
		CcpDbTableField[] fields = this.getFields();
		String[] array = Arrays.asList(fields).stream().map(x -> x.name()).collect(Collectors.toList()).toArray(new String[fields.length]);
		CcpMapDecorator subMap = values.getSubMap(array);
		return subMap;
	}
	
	default CcpMapDecorator save(CcpMapDecorator values) {
		CcpMapDecorator onlyExistingFields = this.getOnlyExistingFields(values);
		CcpDbCrud crud = this.getCrud();
		CcpDbTableField[] fields = this.getFields();
		TimeOption timeOption = this.getTimeOption();
		String id = this.getId(onlyExistingFields, timeOption, fields);

		boolean updated = crud.updateOrSave(onlyExistingFields, this, id);

		this.saveAuditory(id, this.name(), onlyExistingFields, updated);
		
		CcpMapDecorator put = onlyExistingFields.put("_updated",updated).put("_id", id);
		
		return put;
	}
	
	void saveAuditory(String id, String entityName, CcpMapDecorator values, boolean updated);

	TimeOption getTimeOption();

	CcpDbTableField[] getFields();

	CcpDbCrud getCrud();
	
	default CcpMapDecorator remove(CcpMapDecorator values) {
		String id = this.getId(values, this.getTimeOption(), this.getFields());
		CcpMapDecorator remove = this.getCrud().remove(id);
		return remove;
	}

}

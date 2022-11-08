package com.ccp.especifications.db.table;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ccp.decorators.CcpMapDecorator;
import com.ccp.especifications.db.crud.CcpDbCrud;
import com.ccp.exceptions.commons.Flow;
import com.ccp.exceptions.db.CcpRecordNotFound;
import com.ccp.process.CcpProcess;

public interface CcpDbTable {

	
	String name();
	
	
	default String getId(CcpMapDecorator values,TimeOption timeOptioption, CcpDbTableField...keys) {
		
		String formattedCurrentDate = timeOptioption.getFormattedCurrentDate();
		
		if(keys.length == 0) {
			
			if(TimeOption.none != timeOptioption) {
				return formattedCurrentDate;
			}
			
			return UUID.randomUUID().toString();
		}
		
		List<CcpDbTableField> missingKeys = Arrays.asList(keys).stream().filter(key -> values.getAsString(key.name()).trim().isEmpty()).collect(Collectors.toList());
		
		if(missingKeys.isEmpty() == false) {
			throw new Flow(values, 500, "The following keys are missing to compose an id: " + missingKeys + ". Current values: " + values, null);
		}
		
		
		List<String> collect = Arrays.asList(keys).stream().map(key -> values.getAsString(key.name()).trim()).collect(Collectors.toList());
		collect = new ArrayList<>(collect);
		collect.add(0, formattedCurrentDate);
		String replace = collect.toString().replace(",", "_").replace("[", "").replace("]", "");
		return replace;
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
			String id = this.getId(data, this.getTimeOption(), this.getKeys());
			CcpMapDecorator oneById = this.getCrud().getOneById(this, id);
			return oneById;
			
		} catch (CcpRecordNotFound e) {
			CcpMapDecorator execute = ifNotFound.execute(data);
			return execute;
		}
	}

	default boolean exists(CcpMapDecorator data) {
		String id = this.getId(data, this.getTimeOption(), this.getKeys());
		boolean exists = this.getCrud().exists(this, id);
		return exists;
	}
	
	default CcpMapDecorator save(CcpMapDecorator data) {
		String id = this.getId(data, this.getTimeOption(), this.getKeys());
		boolean updated = this.getCrud().updateOrSave(data, this, id);
		CcpMapDecorator put = data.put("_updated",updated);
		return put;
	}

	TimeOption getTimeOption();

	CcpDbTableField[] getKeys();

	CcpDbCrud getCrud();
	
	default void remove(CcpMapDecorator values) {
		String id = this.getId(values, this.getTimeOption(), this.getKeys());
		this.getCrud().remove(id);
	}

}

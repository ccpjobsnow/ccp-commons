package com.ccp.especifications.db.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public enum CcpTimeOption{
	none{
		
		public String getFormattedCurrentDate(Long time) {
			return "";
		}
	}
	,MMyyyy
	,ddMMyyyy
	,ddMMyyyyHH
	,ddMMyyyyHHmm
	,ddMMyyyyHHmmss
	,ddMMyyyyHHmmssSSS, 
	ddMM, 
	weekly{
		
		public String getFormattedCurrentDate(Long date) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(date);
			int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
			int year = cal.get(Calendar.YEAR);
			String string = year + "W" + weekOfYear + "_";
			return string;
		}
	}
	;

	public String getFormattedCurrentDate(Long date) {
		Date d = new Date();
		d.setTime(date);
		String format = new SimpleDateFormat(this.name()).format(d);
		return format + "_";
	}
} 

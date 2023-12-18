package com.ccp.especifications.db.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public enum CcpTimeOption{
	none{
		@Override
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
	ddMM
	;

	public String getFormattedCurrentDate(Long date) {
		Date d = new Date();
		d.setTime(date);
		String format = new SimpleDateFormat(this.name()).format(d);
		return format + "_";
	}
} 

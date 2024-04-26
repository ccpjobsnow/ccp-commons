package com.ccp.decorators;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CcpTimeDecorator {
	
	public final Long time;
	
	public CcpTimeDecorator(Long time) {
		this.time = time;
	}
	public CcpTimeDecorator() {
		this(System.currentTimeMillis());
	}

	public long getSecondsEnlapsedSinceMidnight() {
		Long meiaNoite = this.getMidnight();
		long tempo = (this.time - meiaNoite) / 1000L;
		return tempo;
	}
	
	public int getYear() {
		Calendar instance = Calendar.getInstance();
		instance.setTimeInMillis(this.time);
		int year = instance.get(Calendar.YEAR);
		return year;
	}
	
	public Long getMidnight() {
		Calendar cal = this.getBrazilianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long timeInMillis = cal.getTimeInMillis();
		return timeInMillis;
		
	}
	

	public String getFormattedCurrentDateTime(String pattern) {

		return this.getFormattedDateTime(pattern);
	}

	public String getFormattedDateTime(String pattern) {
		Date d = new Date();
		d.setTime(this.time);
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String format = sdf.format(d);
		return format;
	}


	public Calendar getBrazilianCalendar() {
		TimeZone timeZone = TimeZone.getTimeZone("America/Sao_Paulo");
		Calendar cal = Calendar.getInstance(timeZone);
		return (Calendar)cal.clone();
	}
	
	public void sleep(int i) {
		
		if(i <= 0) {
			return;
		}
		
		try {
			Thread.sleep(i);
		} catch (Exception e) {
		}

	}


}

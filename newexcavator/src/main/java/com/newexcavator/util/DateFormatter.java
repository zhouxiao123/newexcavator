package com.newexcavator.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

public class DateFormatter
{
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public String getCurrentDate()
	{
		return new DateTime(new Date()).toString(DATE_FORMAT);
	}
	
	public String formatDateToStr(Date date) {
		return new DateTime(date).toString(DATE_FORMAT);
	}
	
	public static Date stringToDate(String dt, String format) {
		if (!StringUtils.isBlank(dt)) {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			try {
				return formatter.parse(dt);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String fromNowToDate(Date date) {
		Date now = new Date();
		Long time = now.getTime() - date.getTime();
		//Calendar cal = Calendar.getInstance();
		//cal.setTimeInMillis(time);
		//Integer i = cal.get(Calendar.DAY_OF_YEAR);
		time = time/1000;
		Long m = time/60;
		Long h = m/60;
		Long d = h/24;
		//System.out.println("------"+d);
		if(d==0){
			//System.out.println("+++++"+h);
			if(h==0){
				//Integer m = cal.get(Calendar.MINUTE);
				//System.out.println("======"+m);
				return m+"分钟以前发布";
			} else {
				return h+"小时以前发布";
			}
		} else if(d==1){
			return "昨天发布";
		} else {
			return new DateTime(date).toString("MM-dd 发布");
		}
	}
}

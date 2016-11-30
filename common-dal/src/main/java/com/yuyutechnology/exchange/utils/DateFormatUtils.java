package com.yuyutechnology.exchange.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;

public class DateFormatUtils {
	public static String formatDate(Date date, String pattern)
	{
		try
		{
			if (date == null)
			{
				return "";
			}
			return  org.apache.commons.lang.time.DateFormatUtils.format(date, pattern);
		}
		catch (Exception e)
		{
			//logger.error("error GamingDateUtils.parseString", e);
		}
		return "";
	}
	
	public static Date fromString(String dateString, String pattern){
		Date startTime = null;
		try {
			startTime = new SimpleDateFormat(pattern).parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return startTime;
	}
	
	public static Date getDateWithoutTime(Date date)
	{
		return DateUtils.truncate(date, Calendar.DATE);
	}
	
	public static Date getIntervalDay(Date date, int intervalDay)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtils.truncate(date, Calendar.DATE));
		calendar.add(Calendar.DATE, intervalDay);
		return calendar.getTime();
	}
	
	public static Date getIntervalMinuteGMT8(Date date, int intervalMinute)
	{
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, intervalMinute);
		return calendar.getTime();
	}
	public static String formatDateGMT8(Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return sdf.format(date);
	}
	
	public static Date getStartTime(String start){
		String startStr = start+" 00:00:00";
		Date startTime = null;
		try {
			startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return startTime;
	}
	
	public static Date getEndTime(String start){
		String endStr = start+" 23:59:59";
		Date endTime = null;
		try {
			endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return endTime;
	}
	
	
	public static String[] getWeekDay(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		Date time = null;
		try {
			time = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.setTime(time);
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
		String weekDay [] = new String[7];
		weekDay[0] = sdf.format(cal.getTime());
		for(int i=1; i<7; i++) {
			cal.add(Calendar.DATE, 1);
			weekDay[i] = sdf.format(cal.getTime());
		}
		return weekDay;
	}
	
	public static List<String> getWeekDay2(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		Date time = null;
		try {
			time = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.setTime(time);
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
		List<String> list = new ArrayList<String>();
		list.add(sdf.format(cal.getTime()));
		for(int i=1; i<7; i++) {
			cal.add(Calendar.DATE, 1);
			list.add(sdf.format(cal.getTime()));
		}
		return list;
	}
}

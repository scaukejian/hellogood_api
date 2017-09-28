package com.hellogood.utils;

import java.lang.reflect.Field;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Case;

import com.hellogood.exception.BusinessException;

public class DateUtil {
	
	public static Date stringToDate(String strDate) {
		  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  ParsePosition pos = new ParsePosition(0);
		  Date strtodate = formatter.parse(strDate, pos);
		  return strtodate;
	}
	
	public static String stringDate(String strDate) {
		  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  ParsePosition pos = new ParsePosition(0);
		  Date strtodate = formatter.parse(strDate, pos);
		  return formatter.format(strtodate);
	}
	
	public static String DateToString(Date date) {
		  if(date != null){
			  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  String dateString = formatter.format(date);
			  return dateString;
		  }
		  return null; 
	}
	
	/**
	 * 年月日
	 * @param date
	 * @return
	 */
	public static String DateToStringYMD(Date date) {
		  if(date != null){
			  SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
			  String dateString = formatter.format(date);
			  return dateString;
		  }
		  return null; 
	}
	
	/**
	 * 月日时分秒
	 * @param date
	 * @return
	 */
	public static String DateToStringYRSFM(Date date) {
		  if(date != null){
			  SimpleDateFormat formatter = new SimpleDateFormat("MM.dd HH:MM:SS");
			  String dateString = formatter.format(date);
			  return dateString.substring(0, dateString.length() - 1);
		  }
		  return null; 
	}
	
	
	public static long getTime() {
		  return new Date().getTime();
	}
	
	/**

     * 把毫秒转化成日期

     * @param dateFormat(日期格式，例如：MM/ dd/yyyy HH:mm:ss)

     * @param millSec(毫秒数)

     * @return

     */

	public static Date longToDate(Long millSec){
		return new Date(millSec);
    }
	
	/**
	 * object to map
	 * 日期类型格式化
	 * @param o
	 * @return
	 */
	public static Map<String, Object> object2MapDateFormat(Object o){
		if(o == null)
			return null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Field[] declaredFields  = o.getClass().getDeclaredFields();
			for (Field field : declaredFields) {
				field.setAccessible(true);  
				//过滤内容为空的  
	            if (field.get(o) == null) {  
	                continue;  
	            }  
	            //日期格式统一处理
	            if(field.getType() == Date.class){
	            	Date temp = (Date) field.get(o);
	            	resultMap.put(field.getName(), temp.getTime());
	                continue;
	            }
	            //字段值phone隐藏处理
	            if("phone".equals(field.getName()) || "lastName".equals(field.getName())){
	            	resultMap.put(field.getName(), "");
	                continue;
	            }
	            
	            resultMap.put(field.getName(), field.get(o));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("convert object failed");
		}
		return resultMap;
	}
	
	/**
	 * objects to maps
	 * 日期类型格式化
	 * @param list
	 * @return
	 */
	
	public static List<Map<String, Object>> list2MapDateFormat(List<?> list) {
		if(list == null)
			return null;
		List<Map<String, Object>> resultList = null;
		try {
			resultList = new ArrayList<Map<String, Object>>();
			for (Object o : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				Field[] declaredFields = o.getClass().getDeclaredFields();
				for (Field field : declaredFields) {
					field.setAccessible(true);
					// 过滤内容为空的
					if (field.get(o) == null) {
						continue;
					}
					//日期格式化
					if (field.getType() == Date.class) {
						Date temp = (Date) field.get(o);
						map.put(field.getName(), temp.getTime());
						continue;
					}
					//字段值phone隐藏处理
		            if("phone".equals(field.getName()) || "lastName".equals(field.getName())){
		            	map.put(field.getName(), "");
		                continue;
		            }
		            
					map.put(field.getName(), field.get(o));
				}
				resultList.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException("convert object failed");
		}
		return resultList;
	}
	
	
	/** 计算年龄 */
	public static Integer getAge(Date birthDay){
	        Calendar cal = Calendar.getInstance();
	        if(birthDay == null)
	        	return 0;
	        if (cal.before(birthDay)) {
	        	return 0;
	        }

	        int yearNow = cal.get(Calendar.YEAR);
	      //  int monthNow = cal.get(Calendar.MONTH)+1;
	      //  int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
	       
	        cal.setTime(birthDay);
	        int yearBirth = cal.get(Calendar.YEAR);
	       // int monthBirth = cal.get(Calendar.MONTH);
	       // int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

	        int age = yearNow - yearBirth;

	       /* if (monthNow <= monthBirth) {
	            if (monthNow == monthBirth) {
	                if (dayOfMonthNow < dayOfMonthBirth) {
	                    age--;
	                }
	            } else {
	                age--;
	            }
	        }*/

	        return age;
	    }
	
	/** 计算年龄 */
	public static Integer getAge(String birthDayStr){
			if(StringUtils.isBlank(birthDayStr))
				return 0;
			Date birthDay = new Date(Long.parseLong(birthDayStr));
	        Calendar cal = Calendar.getInstance();
	        if (cal.before(birthDay)) {
	        	return 0;
	        }

	        int yearNow = cal.get(Calendar.YEAR);
	      //  int monthNow = cal.get(Calendar.MONTH)+1;
	      //  int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
	       
	        cal.setTime(birthDay);
	        int yearBirth = cal.get(Calendar.YEAR);
	       // int monthBirth = cal.get(Calendar.MONTH);
	       // int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

	        int age = yearNow - yearBirth;

	       /* if (monthNow <= monthBirth) {
	            if (monthNow == monthBirth) {
	                if (dayOfMonthNow < dayOfMonthBirth) {
	                    age--;
	                }
	            } else {
	                age--;
	            }
	        }*/

	        return age;
	    }

	/**
	 * 
	 * @param age
	 * @param flag=0,计算开始时间
	 * @return
	 */
	public static Date getSearchDate(Integer age,int flag) {
		Calendar cal = Calendar.getInstance();
		
		int yearNow = cal.get(Calendar.YEAR);
		if(flag == 0)
			cal.set(yearNow-age, Calendar.JANUARY, 0);
		else
			cal.set(yearNow-age,Calendar.DECEMBER,31);
		return cal.getTime();
	} 
	
	public static int getSecondsMinusDate(Date date){
		return (int)((System.currentTimeMillis()-date.getTime())/1000);
	}
	
	public static String getShowTimeStr(Date date){
		int seconds = getSecondsMinusDate(date);
		if(seconds<60){
			return seconds+"秒前";
		}else if(seconds<3600){
			return seconds/60+"分钟前";
		}else if(seconds<60*60*24){
			return seconds/3600+"小时前";
		}else{
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return (cal.get(Calendar.MONTH)+1)+"月"+cal.get(Calendar.DAY_OF_MONTH)+"日";
		}
	}
	
	public static String getGroupPacketTimeStr(Date startDate, Date endDate){
		if(endDate == null || startDate == null)
			return "红包被抢光了";
		int seconds = (int)(endDate.getTime() - startDate.getTime())/1000;
		if(seconds<60){
			return seconds+"秒被抢光";
		}else if(seconds<3600){
			return seconds/60+"分被抢光";
		}else if(seconds<60*60*24){
			return seconds/3600+"小时被抢光";
		}else{
			return seconds/(3600*24)+"天被抢光";
		}
	}
	
	public static String getChineseMonthName(String month){
		 switch (month) {
         case "01":
             return "一月";
         case "02":
        	 return "二月";
         case "03":
        	 return "三月";
         case "04":
        	 return "四月";
         case "05":
        	 return "五月";
         case "06":
        	 return "六月";
         
         case "07":
        	 return "七月";
         case "08":
        	 return "八月";
         case "09":
        	 return "九月";
         case "10":
        	 return "十月";
         case "11":
        	 return "十一月";
         case "12":
        	 return "十二月";
        	 
         default:
        	 return "xx";
		 }
	}
	 
	
	 
	/**
	 * 凌晨
	 * @param date
	 * @flag 0 返回yyyy-MM-dd 00:00:00日期<br>
	 *       1 返回yyyy-MM-dd 23:59:59日期
	 * @return
	 */
	public static Date weeHours(Date date, int flag) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int hour = cal.get(Calendar.HOUR_OF_DAY);
	    int minute = cal.get(Calendar.MINUTE);
	    int second = cal.get(Calendar.SECOND);
	    //时分秒（毫秒数）
	    long millisecond = hour*60*60*1000 + minute*60*1000 + second*1000;
	    //凌晨00:00:00
	    cal.setTimeInMillis(cal.getTimeInMillis()-millisecond);
	      
	    if (flag == 0) {
	        return cal.getTime();
	    } else if (flag == 1) {
	        //凌晨23:59:59
	        cal.setTimeInMillis(cal.getTimeInMillis()+23*60*60*1000 + 59*60*1000 + 59*1000);
	    }
	    return cal.getTime();
	}

	public static String getDayAndMonth(Date date){
		int day = date.getDate();
		int month = date.getMonth()+1;
		StringBuffer dayAndMonth = new StringBuffer();
		if(day < 10)
			dayAndMonth.append("0"+day);
		else
			dayAndMonth.append(day);
		
		if(month < 10)
			dayAndMonth.append("0"+month);
		else
			dayAndMonth.append(month);
		
		return dayAndMonth.append("月").toString();
	}
	
	
	//星座
	public static final String[] constellationArr = { "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座" };
	 
	public static final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
	 
	/**
	 * 根据日期获取星座
	 * @return
	 */
	public static String getConstellation(Date date) {
	    if (date == null) {
	        return "";
	    }
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    int month = cal.get(Calendar.MONTH);
	    int day = cal.get(Calendar.DAY_OF_MONTH);
	    if (day < constellationEdgeDay[month]) {
	        month = month - 1;
	    }
	    if (month >= 0) {
	        return constellationArr[month];
	    }
	    // default to return 魔羯
	    return constellationArr[11];
	}
	
	/**
	 * 返回一个月前的时间
	 * @return
	 */
	public static Date getOneMonthEarlyTime(){
		Calendar c = Calendar.getInstance();
		Date date=new Date();
		c.setTime(date);
		c.add(Calendar.MONTH,-1);
		return c.getTime();
	}
	
	/**
	 * 返回一个月后的时间
	 * @return
	 */
	public static Date getOneMonthLaterTime(){
		Calendar c = Calendar.getInstance();
		Date date=new Date();
		c.setTime(date);
		c.add(Calendar.MONTH,1);
		return c.getTime();
	}
	/**
	 * 返回一天后的时间
	 * @return
	 */
	public static long getOneDayLaterTime(Date time){
		Calendar c = Calendar.getInstance();
		Date date=new Date(time.getTime());
		c.setTime(date);
		c.add(Calendar.DATE,+1);
		return c.getTime().getTime();
	}
	/**
	 * 返回一周后的时间
	 * @return
	 */
	public static long getOneWeekLaterTime(Date time){
		Calendar c = Calendar.getInstance();
		Date date=new Date(time.getTime());
		c.setTime(date);
		c.add(Calendar.DATE,+7);
		return c.getTime().getTime();
	}
	
	/**
	 * 返回一天前的时间
	 * @return
	 */
	public static Date getOneDayEarlyTime(){
		Calendar c = Calendar.getInstance();
		Date date=new Date();
		c.setTime(date);
		c.add(Calendar.DATE,-1);
		return c.getTime();
	}
	/**
	 * 返回一周前的时间
	 * @return
	 */
	public static Date getOneWeekEarlyTime(){
		Calendar c = Calendar.getInstance();
		Date date=new Date();
		c.setTime(date);
		c.add(Calendar.DATE,-7);
		return c.getTime();
	}
	
	public static void main(String[] args){
		Date date = stringToDate("1991-06-16 00:00:00");
		System.out.println(getConstellation(date));
	}
	
}

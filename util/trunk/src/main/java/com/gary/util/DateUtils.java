package com.gary.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.gary.util.dto.MonthDate;
import com.gary.util.dto.WeekDate;
/**
 * 日期时间工具类
 * @author Gary
 *
 */
public class DateUtils {
	
	private static final Integer VALID_DATE_VALUE = 1;
	
	private static final long GTM = 8*60*60*1000;//8小时
	
	private static final String HH_MM_SS = "HH:mm:ss";
	
	/**
	 * 增加一月
	 * @author:Gary
	 * @param begin
	 * @return
	 */
	public static Date getValidDate(Date begin) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(begin);
		cal.add(Calendar.MONTH, VALID_DATE_VALUE);
		return cal.getTime();
	}
	
	/**
	 * 增加一天
	 * @author:Gary
	 * @return Date
	 */
	public static Date addDay(Date source) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(source);
		cal.add(Calendar.DATE, VALID_DATE_VALUE);
		return cal.getTime();
	}
	/**
	 * 增加N天
	 * @author:Gary
	 * @return Date
	 */
	public static Date addDay(Date source,int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(source);
		cal.add(Calendar.DATE, n);
		return cal.getTime();
	}
	
	/**
	 * 增加相应的月数
	 * @author:Gary
	 * @param begin
	 * @return Date
	 */
	public static Date getValidDate(Date begin, Integer planValue) {
		Calendar cal = Calendar.getInstance();
		Date nowDate = new Date();
		cal.setTime(begin.before(nowDate)?nowDate:begin);
		cal.add(Calendar.MONTH, planValue);
		return cal.getTime();
	}
	
	/**
	 * 把秒转化为时间
	 * @author:Gary
	 * @param time 秒
	 * @param f 格式
	 * @return f 格式的String
	 */
	public static String getTimeStr(long time,String f) {
		SimpleDateFormat sdf = new SimpleDateFormat(f);
		Date d = new Date(time*1000 - GTM); 
		return sdf.format(d);
	}
	/**
	 * 把秒转化为时间
	 * @author:Gary
	 * @param time 秒
	 * @return HH_MM_SS 格式的String
	 */
	public static String getTimeStr(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(HH_MM_SS);
		Date d = new Date(time*1000 - GTM); 
		return sdf.format(d);
	}
	/**
	 * @desc:把毫秒转化为时间
	 * @auth:Gary
	 * @Date:2011-7-19 下午03:04:58
	 * @param time
	 * @return 00:00:00
	 */
	public static String getTimeStrByHao(long time,String f) {
		SimpleDateFormat format = new SimpleDateFormat(f);
		return format.format(new Date(time-GTM));
	}
	/**
	 * @desc:把毫秒转化为时间
	 * @auth:Gary
	 * @Date:2011-7-19 下午03:04:58
	 * @param time
	 * @return 00:00:00
	 */
	public static String getTimeStrByHao(long time) {
		SimpleDateFormat format = new SimpleDateFormat(HH_MM_SS);
		return format.format(new Date(time-GTM));
	}
	/**
	 * 算出当前日期的周一与周日的日期
	 * @return 星期实体
	 */
	public static WeekDate dayOfWeek(){ 
		WeekDate wd=new WeekDate();
        Date today=new Date(); 
        Calendar cal=Calendar.getInstance(); 
        cal.setTime(today); 
        int between=cal.get(Calendar.DAY_OF_WEEK)-1; 
        int subMonday=0; 
        if (between>=1&&between <=6){ 
            subMonday=1-between; 
        }else if (between==0) { 
            subMonday=-6; 
        } 
        cal.add(Calendar.DAY_OF_MONTH,subMonday); 
        wd.setMonday(new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime()).trim());
        cal.add(Calendar.DAY_OF_MONTH,6); 
        wd.setSunday(new SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime()).trim());
        return wd;
    }
	/**
	 * 得到某年某月的第一天和最后一天的日期
	 * @return 月实体
	 */
	public static MonthDate dayOfMonth(int year,int month){
		MonthDate md=new MonthDate();
		Calendar   c   =   Calendar.getInstance(); 
		c.set(year, month-1, 1);		
		c.set(Calendar.DATE,1);
		md.setFirstday(new SimpleDateFormat( "yyyy-MM-dd ").format(c.getTime()).trim());
		c.set(Calendar.DATE,c.getActualMaximum(Calendar.DAY_OF_MONTH));
		md.setLastday(new SimpleDateFormat( "yyyy-MM-dd ").format(c.getTime()).trim());
		return md;
	}
	/**
	 * 计算日期为当年的第几周
	 * @param date Date
	 * @return int
	 */
	public static int getWeekOfDate(Date date){
		  Calendar calendar = Calendar.getInstance();
		  calendar.setFirstDayOfWeek(Calendar.MONDAY);
		  calendar.setTime(date);
		  return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	/**
	 * 计算日期为当年的第几周
	 * @param today 格式：2012-12-24
	 * @return int
	 * @throws ParseException
	 */
	public static int getWeekOfDate(String today) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		  Date date = format.parse(today);
		  Calendar calendar = Calendar.getInstance();
		  calendar.setFirstDayOfWeek(Calendar.MONDAY);
		  calendar.setTime(date);
		  return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	/**
	 * 获取某月有几天
	 * @param year
	 * @param month
	 * @return int
	 */
	public static int getMonthDays(int year, int month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR,year);
		cal.set(Calendar.MONTH, month - 1);//Java月份才0开始算
		return cal.getActualMaximum(Calendar.DATE);
	}
	/**
	 * 计算两个日期之间的周数
	 * @param strDateStart Date
	 * @param strDateEnd Date
	 * @return long
	 */
	public static long getWeeksBetweenDates(Date strDateStart, Date strDateEnd){
		long week=strDateEnd.getTime()>strDateStart.getTime()?(strDateEnd.getTime()- strDateStart.getTime())/86400000/7:(strDateStart.getTime()- strDateEnd.getTime())/86400000/7;
		return week;
	}
	/**
	 * 计算两个日期之间的周数
	 * @param strDateStart 格式：2012-12-24
	 * @param strDateEnd 格式：2012-12-24
	 * @return long
	 * @throws ParseException
	 */
	public static long getWeeksBetweenDates(String strDateStart, String strDateEnd) throws ParseException{
		Date dateStart = new SimpleDateFormat("yyyy-MM-dd").parse(strDateStart );
		Date dateEnd = new SimpleDateFormat("yyyy-MM-dd").parse(strDateEnd );
		long week=dateEnd.getTime()>dateStart.getTime()?(dateEnd.getTime()- dateStart.getTime())/86400000/7:(dateStart.getTime()- dateEnd.getTime())/86400000/7;
		return week;
	}
	/**
	 * 返回当月week数 
	 * @param year
	 * @param month
	 * @param week 1~7
	 * @return int 
	 */
	public static int getSundays(int year,int month,int week){
		int sundays=0;
		week++;
		week=week==8?1:week;
		Calendar setDate= Calendar.getInstance();
		//		从第一天开始
		int day;
		for(day=1;day<=getMonthDays(year,month);day++){
			setDate.set(Calendar.DATE,day);
			if(setDate.get(Calendar.DAY_OF_WEEK)==week){
				sundays++;
			}
		}
		return sundays;
	} 
	/**
	 * 根据给定的某年的周数得到一周的日期
	 * @param year
	 * @param week
	 * @return
	 * @throws ParseException
	 */
	public static Map<Integer, String> getWeekDateBuyYear(int year,int week) throws ParseException{
		Map<Integer, String> map=new HashMap<Integer, String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date dt = format.parse(year+"-01-01");
        calendar.setTime(dt);
        calendar.add(Calendar.WEEK_OF_YEAR, week-1);
        for (int i=0; i<7; i++) {
            dt = calendar.getTime();
            map.put(i+1, format.format(dt));
            calendar.add(Calendar.DATE, 1);
        }
        return map;
	}
	/**
	 * 根据日期得到星期几
	 * @param d Date
	 * @return int
	 */
	public static int getWeekByDate(Date d){
	    Calendar cd = Calendar.getInstance();
	    cd.setTime(d);
	    int day_of_week = cd.get(Calendar.DAY_OF_WEEK) - 1 ;
	    if(day_of_week==0){
		     day_of_week += 7 ;
	    }
	    return day_of_week;
	}
	/**
	 * 根据日期得到星期几
	 * @param d 2012-12-24
	 * @return int
	 * @throws ParseException 
	 */
	public static int getWeekByDate(String date) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = sdf.parse(date);
	    Calendar cd = Calendar.getInstance();
	    cd.setTime(d);
	    int day_of_week = cd.get(Calendar.DAY_OF_WEEK) - 1 ;
	    if(day_of_week==0){
		     day_of_week += 7 ;
	    }
	    return day_of_week;
	}
	/**
	 * 计算day天后的日期
	 * @param d Date
	 * @param day int
	 * @return Date
	 */
	public static Date getDateByDays(Date d,int day){
		Calendar cd = Calendar.getInstance();
	    cd.setTime(d);
	    cd.add(Calendar.DAY_OF_YEAR ,day);
	    return cd.getTime();
	}
	/**
	 * 日期减去天数
	 * @param d Date
	 * @param day int
	 * @return Date
	 */
	public static Date subDate(Date d,int day){
		Calendar cd = Calendar.getInstance();
	    cd.setTime(new Date());
	    cd.add(Calendar.DAY_OF_YEAR ,-day);
	    return cd.getTime();
	}
	/**
	 * 计算两个日期之间的天数
	 * @param d1 Date 
	 * @param d2 Date 
	 * @return long
	 */
	public static long getDayBySubDate(Date d1,Date d2){
		long daysBetween=d2.getTime()>d1.getTime()?(d2.getTime()-d1.getTime()+1000000)/(3600*24*1000):(d1.getTime()-d2.getTime()+1000000)/(3600*24*1000);
		return daysBetween;
	}
	public static void main(String[] args) {
		System.out.println(DateUtils.getTimeStr(60*60*25));
	}
}

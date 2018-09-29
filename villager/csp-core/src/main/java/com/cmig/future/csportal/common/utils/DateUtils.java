/**
 * .
 */
package com.cmig.future.csportal.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}
	/**
	 * 得到当天小时字符串 格式（HH）
	 */
	public static String getHours() {
		return formatDate(new Date(), "HH");
	}
	/**
	 * 得到分钟字符串 格式（mm）
	 */
	public static String getMinutes() {
		return formatDate(new Date(), "mm");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
	
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	
	/**
	* 此方法描述的是：添加一天
	* @author:jinghao.che@zymobi.com
	* @param date
	* @param num
	* @return Date
	*/
	public Date addDay(Date date, int num) {
	  Calendar startDT = Calendar.getInstance();
	  startDT.setTime(date);
	  startDT.add(Calendar.DAY_OF_MONTH, num);
	  return startDT.getTime();
	}
	
	/**
	* 此方法描述的是：获取两个日期之间的秒数
	* @author:zhangtao107@126.com
	* @param before
	* @param after
	* @return long
	*/
	public static long getDistanceSecondOfTwoDate(Date before, Date after){
		// 当前系统时间毫秒值	
		long beforeLong = before.getTime();
		long afterLong = after.getTime();
		long minute = (afterLong - beforeLong) /1000;
		return minute;
	}
	
	/**
	* 此方法描述的是：获取两个日期之间的分钟数
	* @author:zhangtao107@126.com
	* @param before
	* @param after
	* @return long
	*/
	public static long getDistanceMinuteOfTwoDate(Date before, Date after){
		long minute = getDistanceSecondOfTwoDate(before,after) /60;
		return minute;
	}
	
	public static int getAge(Date birthDay) throws Exception { 
        //获取当前系统时间
        Calendar cal = Calendar.getInstance(); 
        //如果出生日期大于当前时间，则抛出异常
        if (cal.before(birthDay)) { 
            throw new IllegalArgumentException( 
                "The birthDay is before Now.It's unbelievable!"); 
        } 
        //取出系统当前时间的年、月、日部分
        int yearNow = cal.get(Calendar.YEAR); 
        int monthNow = cal.get(Calendar.MONTH); 
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); 
         
        //将日期设置为出生日期
        cal.setTime(birthDay); 
        //取出出生日期的年、月、日部分  
        int yearBirth = cal.get(Calendar.YEAR); 
        int monthBirth = cal.get(Calendar.MONTH); 
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH); 
        //当前年份与出生年份相减，初步计算年龄
        int age = yearNow - yearBirth; 
        //当前月份与出生日期的月份相比，如果月份小于出生月份，则年龄上减1，表示不满多少周岁
        if (monthNow <= monthBirth) { 
            //如果月份相等，在比较日期，如果当前日，小于出生日，也减1，表示不满多少周岁
            if (monthNow == monthBirth) { 
                if (dayOfMonthNow < dayOfMonthBirth) age--; 
            }else{ 
                age--; 
            } 
        } 
        return age; 
    }

    /**
     * Unix时间戳转化为Date
     * @param longTime
     * @return
     */
    public static Date unixTimeToDate(Long longTime){
        Date date= null;
        try {
            Long timestamp = longTime * 1000;
            date=new Date(timestamp);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


	/**
	 * 时间戳转化为Date
	 * @param timestamp
	 * @return
	 */
	public static Date longToDate(Long timestamp){
		Date date= null;
		try {
			date=new Date(timestamp);
			return date;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取今天还剩下多少秒
	 * @return
	 */
	public static int getTodayLeftSecond(){
		Calendar curDate = Calendar.getInstance();
		Calendar tommorowDate = new GregorianCalendar(curDate
				.get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate
				.get(Calendar.DATE) + 1, 0, 0, 0);
		return (int)(tommorowDate.getTimeInMillis() - curDate .getTimeInMillis()) / 1000;
	}

	/**
	 * 获取今天还剩下多少秒
	 * @return
	 */
	public static int getTodayLeftSecond(Calendar curDate){
		Calendar tommorowDate = new GregorianCalendar(curDate
				.get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate
				.get(Calendar.DATE) + 1, 0, 0, 0);
		return (int)(tommorowDate.getTimeInMillis() - curDate .getTimeInMillis()) / 1000;
	}

	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
		System.out.println(getDistanceMinuteOfTwoDate(parseDate("2016-03-14 15:10:00"),new Date()));
		
		Date expireDate = DateUtils.addDays(new Date(), 1);
		String year = DateUtils.formatDate(expireDate, "yyyy");
		String month = DateUtils.formatDate(expireDate, "MM");
		String day =  DateUtils.formatDate(expireDate, "dd");
		String h = DateUtils.formatDate(expireDate, "HH");
		String minutes =  DateUtils.formatDate(expireDate, "mm");
		String strs[] = {year,month,day,h,minutes,"4008115251"};
		for (int i = 0; i < strs.length; i++) {
			System.out.println(strs[i]);
		}
		
	}
}

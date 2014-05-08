package com.zjhcsoft.qin.exchange.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
/**
 * 日期工具类
 * @author Administrator
 *
 */
public class DateUtils {

    private static DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.SIMPLIFIED_CHINESE);

    private static DateFormat yyyyMMddFormatNoSplit = new SimpleDateFormat("yyyyMMdd");

    private static DateFormat yyyyMMddFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static DateFormat yyyyMMFormatNoSplit = new SimpleDateFormat("yyyyMM");

    private static DateFormat yyyyMMDDhhmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static DateFormat yyyyMMddHHmmssF = new SimpleDateFormat("yyyyMMddHHmmss");


    public static synchronized String getDateYYYYMMDD(Date date) {
        return yyyyMMddFormatNoSplit.format(date);
    }

    public static synchronized String getDateYYYYMMDD() {
        return yyyyMMddFormatNoSplit.format(new Date());
    }

    public static synchronized String getDateYYYYMM() {
        return yyyyMMFormatNoSplit.format(new Date());
    }

    public static synchronized String getDateYYYYMMDDhhmmss(Date date) {
        return yyyyMMDDhhmmss.format(date);
    }

    public static synchronized String getDateYYYYMMDDhhmmss() {
        return yyyyMMDDhhmmss.format(new Date());
    }

    public static synchronized String getDateYYYYMMDDhhmmssF() {
        return yyyyMMddHHmmssF.format(new Date());
    }
	/**
	 * 获取当前时分秒
     * @return
	 */
	public static String getNowTime() {
        GregorianCalendar gcNow = new GregorianCalendar();
        java.util.Date dNow = gcNow.getTime();
        DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.SIMPLIFIED_CHINESE);
        return df.format(dNow);
    }

	public static String formatSDate(java.util.Date date) {
        if (date == null)
            return "";
        else {
            SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return bartDateFormat.format(date);
        }
    }

	/**
	 * 获取当前时间 yyyyMMdd
     * @return
	 */
	public static String getNowDate() {
        GregorianCalendar gcNow = new GregorianCalendar();
        java.util.Date dNow = gcNow.getTime();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.SIMPLIFIED_CHINESE);
        return df.format(dNow);
    }
	/**
	 * 获取当前时间 eg:2012-8-10 16:31:09
     * @return
	 */
	public static String getNowDateTime() {
        GregorianCalendar gcNow = new GregorianCalendar();
        java.util.Date dNow = gcNow.getTime();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, Locale.SIMPLIFIED_CHINESE);
        return df.format(dNow);
    }

	/**
	 * 字符串转换到时间格式
	 * @param dateStr 需要转换的字符串
	 * @param formatStr 需要格式的目标字符串  举例 yyyy-MM-dd
	 * @return Date 返回转换后的时间
	 * @throws java.text.ParseException 转换异常
	 */
	public static Date StringToDate(String dateStr,String formatStr){
		DateFormat sdf=new SimpleDateFormat(formatStr);
		Date date=null;
		try {
			date = sdf.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
    }
	
	public static Timestamp getTimestamp(String str) {
        return Timestamp.valueOf(DateUtils.getDateYYYYMMDDhhmmss(StringToDate(str, "yyyy-MM-dd HH:mm:ss")));
	}
	
	public static void main(String[] args) {
		Date aa = DateUtils.StringToDate("2012-9-12 12:21:12", "yyyy-MM-dd HH:mm:ss");
		String daStr = DateUtils.getDateYYYYMMDDhhmmss(aa);
		System.out.println(daStr);
		
		String s = getNowDateTime();//字符串时间
		System.out.println(getTimestamp(s));
		
		System.out.println(getDateYYYYMMDDhhmmssF());
		
		Date bb = DateUtils.StringToDate("201233", "yyyyMM");
		System.out.println(bb);

	}
}

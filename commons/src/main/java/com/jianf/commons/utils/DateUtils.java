package com.jianf.commons.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTime.Property;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author: fengjian
 * @ClassName: DateUtils
 * @Description: 时间日期工具类
 * @date 2016年7月24日 下午7:57:41
 */
public class DateUtils {

    private DateUtils() {

    }

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIMEZ_PATTERN = "yyyy-MM-dd HH:mm:ss.SSSZ";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String MONTH_PATTERN = "yyyy-MM";
    public static final String MONTHPATTERN = "yyyyMM";
    public static final String DATEPATTERN = "yyyyMMdd";
    public static final String HOURS_PATTERN = "HH:mm:ss";
    public static final String FIRST_TIME = " 00:00:01";
    public static final String LAST_TIME = " 23:59:59";
    public static final long DAYS = 60 * 60 * 24;
    public static final long HOURS = 60 * 60;


    public static LocalDateTime convertLongToDate(Long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp.longValue()), ZoneId.systemDefault());
    }

    public static LocalDateTime convertDateToDate(Date timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp.getTime()), ZoneId.systemDefault());
    }

    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static boolean isAfterSeconds(Date date, long seconds) {
        LocalDateTime beginTime = DateUtils.convertDateToDate(date);

        LocalDateTime now = LocalDateTime.now();

        return now.isAfter(beginTime.plusSeconds(seconds));
    }

    public static boolean isAfterSeconds(LocalDateTime beginTime, long seconds) {
        LocalDateTime now = LocalDateTime.now();

        return now.isAfter(beginTime.plusSeconds(seconds));
    }

    /**
     * 获取当前日期
     */
    public static Date getDate() {
        return new DateTime().toDate();
    }

    /**
     * 日期转换dateTime
     */
    public static DateTime getDateTime(Date date) {
        return new DateTime(date);
    }

    /**
     * 日期转换为字符串yyyy-MM-dd
     */
    public static String dateformatString(Date date) {
        return DateUtils.date2String(date, null);
    }

    /**
     * 指定格式日期转字符串
     */
    public static String date2String(Date date, String pattern) {
        return DateUtils.getDateTime(date).toString(StringUtils.isNotBlank(pattern) ? pattern : DATE_PATTERN);
    }

    /**
     * 得到以yyyyMMdd格式表示的当前日期字符串
     */
    public static String getDatePatternConnect() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATEPATTERN);
        return sdf.format(new Date());
    }

    /**
     * 字符串转日期
     */
    public static Date stringFormatDate(String date, String pattern) {
        DateTimeFormatter format = DateTimeFormat.forPattern(StringUtils.isNotBlank(pattern) ? pattern : DATE_PATTERN);
        DateTime dateTime = DateTime.parse(date, format);
        return dateTime.toDate();
    }

    /**
     * 字符串时间
     *
     * @return
     */
    public static Date parse(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date stringFormatDate2(String date, String pattern) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern(pattern);
        LocalDate holiday = LocalDate.parse(date, formatter);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = holiday.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }

    public static Date stringFormatDateTime(String date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    /**
     * 字符串转日期
     */
    public static Date stringFormatDate(String date) {
        return DateUtils.stringFormatDate(date, null);
    }

    /**
     * 计算两个日期时间差(天)
     */
    public static int gapDays(Date date, Date date1) {
        Period p = new Period(DateUtils.getDateTime(date), DateUtils.getDateTime(date1), PeriodType.days());
        return p.getDays();
    }

    /**
     * 计算两个日期时间差(月)
     */
    public static int gapMonths(Date date, Date date1) {
        Period p = new Period(DateUtils.getDateTime(date), DateUtils.getDateTime(date1), PeriodType.months());
        return p.getMonths();
    }

    /**
     * 计算两个日期时间差(年)
     */
    public static int gapYears(Date date, Date date1) {
        Period p = new Period(DateUtils.getDateTime(date), DateUtils.getDateTime(date1), PeriodType.years());
        return p.getYears();
    }

    /**
     * 计算两个日期时间差(小时)
     */
    public static int gapHours(Date date, Date date1) {
        Period p = new Period(DateUtils.getDateTime(date), DateUtils.getDateTime(date1), PeriodType.hours());
        return p.getHours();
    }

    /**
     * 计算两个日期时间差(分)
     */
    public static int gapMinutes(Date date, Date date1) {
        Period p = new Period(DateUtils.getDateTime(date), DateUtils.getDateTime(date1), PeriodType.minutes());
        return p.getMinutes();
    }

    /**
     * 计算两个日期时间差(秒)
     */
    public static int gapSeconds(Date date, Date date1) {
        Period p = new Period(DateUtils.getDateTime(date), DateUtils.getDateTime(date1), PeriodType.seconds());
        return p.getSeconds();
    }

    /**
     * 计算两个日期时间差(秒)
     */
    public static long gapMillis(Date date, Date date1) {
        Duration d = new Duration(DateUtils.getDateTime(date), DateUtils.getDateTime(date1));
        return d.getStandardSeconds();
    }

    /**
     * 获取N天前日期
     */
    public static Date minusDays(Date date, int day) {
        return DateUtils.getDateTime(date).minusDays(day).toDate();
    }

    /**
     * 获取N天后日期
     */
    public static Date plusDays(Date date, int day) {
        return DateUtils.getDateTime(date).plusDays(day).toDate();
    }

    /**
     * 获取N个月前日期
     */
    public static Date minusMonths(Date date, int month) {
        return DateUtils.getDateTime(date).minusMonths(month).toDate();
    }

    /**
     * 获取N个月后日期
     */
    public static Date plusMonths(Date date, int month) {
        return DateUtils.getDateTime(date).plusMonths(month).toDate();
    }

    /**
     * 获取N年前日期
     */
    public static Date minusYears(Date date, int year) {
        return DateUtils.getDateTime(date).minusYears(year).toDate();
    }

    /**
     * 获取N年后日期
     */
    public static Date plusYears(Date date, int year) {
        return DateUtils.getDateTime(date).plusYears(year).toDate();
    }

    /**
     * 获取N周前日期
     */
    public static Date minusWeeks(Date date, int week) {
        return DateUtils.getDateTime(date).minusWeeks(week).toDate();
    }

    /**
     * 获取N周前日期
     */
    public static Date plusWeeks(Date date, int week) {
        return DateUtils.getDateTime(date).plusWeeks(week).toDate();
    }

    /**
     * 获取N小时前日期
     */
    public static Date minusHours(Date date, int hour) {
        return DateUtils.getDateTime(date).minusHours(hour).toDate();
    }

    /**
     * 获取N小时后日期
     */
    public static Date plusHours(Date date, int hour) {
        return DateUtils.getDateTime(date).plusHours(hour).toDate();
    }

    /**
     * 是否比系统时间大
     */
    public static boolean isAfterNow(Date date) {
        return DateUtils.getDateTime(date).isAfterNow();
    }

    /**
     * 是否比系统时间小
     */
    public static boolean isBeforeNow(Date date) {
        return DateUtils.getDateTime(date).isBeforeNow();
    }

    /**
     * 是否与系统时间相等
     */
    public static boolean isEqualNow(Date date) {
        return DateUtils.getDateTime(date).isEqualNow();
    }

    /**
     * 时间比较date大于date1
     */
    public static boolean isEqualNow(Date date, Date date1) {
        return DateUtils.getDateTime(date).isAfter(DateUtils.getDateTime(date1));
    }

    /**
     * 时间比较date小于date1
     */
    public static boolean isBefore(Date date, Date date1) {
        return DateUtils.getDateTime(date).isBefore(DateUtils.getDateTime(date1));
    }

    /**
     * 时间比较date等于date1
     */
    public static boolean isEqual(Date date, Date date1) {
        return DateUtils.getDateTime(date).isEqual(DateUtils.getDateTime(date1));
    }

    /**
     * 获取日期中年份
     */
    public static int getYear() {
        return getYear(getDate());
    }

    /**
     * 获取日期中年份
     */
    public static int getYear(Date date) {
        return new DateTime(date).getYear();
    }


    /**
     * 获取当前日期中月份
     */
    public static int getMonth() {
        return getMonth(getDate());
    }

    /**
     * 获取当前日期中月份
     */
    public static int getMonth(Date date) {
        return new DateTime(date).getMonthOfYear();
    }

    /**
     * 获取日期中日份
     */
    public static int getDay(Date date) {
        return new DateTime(date).getDayOfMonth();
    }

    /**
     * 获取当前日期中日份
     */
    public static int getDay() {
        return getDay(getDate());
    }


    public static int getHours(Date date) {
        return new DateTime(date).getHourOfDay();
    }

    public static int getMinute(Date date) {
        return new DateTime(date).getMinuteOfHour();
    }

    public static int getSecond(Date date) {
        return new DateTime(date).getSecondOfMinute();
    }

    /**
     * 判断是否为闰年
     */
    public static boolean isLeap(Date date) {
        Property month = DateUtils.getDateTime(date).monthOfYear();
        return month.isLeap();
    }

    /**
     * 获取当前时间到第二天凌晨时间剩余秒数
     */
    public static long surplusSeconds() {
        Date date = DateUtils.plusDays(new Date(), 1);
        DateTime dateTime1 = new DateTime(DateUtils.stringFormatDate(DateUtils.date2String(date, "yyyy-MM-dd")));
        return DateUtils.gapMillis(new Date(), dateTime1.toDate());
    }

    /**
     * 获取指定时间年
     *
     * @param date
     * @return
     */
    public static Integer getYear(String date) {
        LocalDate ld = LocalDate.parse(date);
        return Integer.parseInt(String.valueOf(ld.getYear()));
    }

    /**
     * 获取指定时间月
     *
     * @param date
     * @return
     */
    public static Integer getMonth(String date) {
        LocalDate ld = LocalDate.parse(date);
        return Integer.parseInt(String.valueOf(ld.getMonth().getValue()));
    }

    /**
     * 获取指定时间日
     *
     * @param date
     * @return
     */
    public static Integer getDayOfMonth(String date) {
        LocalDate ld = LocalDate.parse(date);
        return Integer.parseInt(String.valueOf(ld.getDayOfMonth()));
    }

    /**
     * 得到以yyyy-MM-dd格式
     *
     * @return
     */
    public static String getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        return sdf.format(new Date());
    }

    /**
     * 获取预计放款时间
     * 首次还款小于15天加一个月，大于45天减一个月
     *
     * @param date
     * @param repaymentDay
     * @return
     */
    public static Date getPassDate(Date date, Integer repaymentDay) {
        //加一个月账单日
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(DateUtils.plusMonths(date, 1));
        calendar.set(Calendar.DATE, repaymentDay);
        Date repayDate = calendar.getTime();
        int gapDays = DateUtils.gapDays(date, repayDate);
        System.out.println(gapDays);
        if (gapDays < 15) {
            //小于15天加一个月
            //repayDate = repayDate;
        } else if (gapDays > 45) {
            //大于45天减一个月
            repayDate = DateUtils.minusMonths(repayDate, 2);
        } else {
            repayDate = DateUtils.minusMonths(repayDate, 1);
        }
        return repayDate;
    }

    public static void main(String[] args) {
        Date date = DateUtils.stringFormatDate("2018-04-01");
        Date date1 = DateUtils.stringFormatDate("2018-04-02");
        Date repayDate = DateUtils.getPassDate(date, 17);

        System.out.println(repayDate);

        System.out.println(DateUtils.gapMinutes(date, date1));
    }
}

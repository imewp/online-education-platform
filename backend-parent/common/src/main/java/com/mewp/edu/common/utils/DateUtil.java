package com.mewp.edu.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 日期时间工具类
 *
 * @author mewp
 * @version 1.0
 * @date 2023/8/19 23:26
 */
public class DateUtil {

    public static final String YYYY_MM_DD_HH_MM_SS_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_FORMAT = "yyyy-MM-dd";
    public static final String YYYYMMDD_FORMAT = "yyyyMMdd";
    public static final String HHMMSS_FORMAT = "HHmmss";
    public static final String YYYYMM_FORMAT = "yyyyMM";
    public static final String YYYY_MM_FORMAT = "yyyy_MM";

    public DateUtil() {
    }

    /**
     * 将日期时间类型转换成通用字符串格式
     *
     * @param dateTime 日期时间
     * @return 时间字符串
     */
    public static String toDateTime(LocalDateTime dateTime) {
        return toDateTime(dateTime, YYYY_MM_DD_HH_MM_SS_FORMAT);
    }

    /**
     * 将日期时间类型转换成指定字符串格式
     *
     * @param dateTime 日期时间
     * @param pattern  字符串格式
     * @return 时间字符串
     */
    public static String toDateTime(LocalDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
    }

    /**
     * 将日期类型转换成指定字符串格式
     *
     * @param date    日期
     * @param pattern 字符串格式
     * @return 日期字符串
     */
    public static String toDateText(LocalDate date, String pattern) {
        if (Objects.isNull(date) || Objects.isNull(pattern)) {
            return null;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern, Locale.SIMPLIFIED_CHINESE));
    }

    /**
     * 求指定小时数后的时间
     *
     * @param date 时间
     * @param hour 小时
     * @return 时间
     */
    public static Date addExtraHour(Date date, int hour) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.HOUR_OF_DAY, hour);
        return cal.getTime();
    }

    /**
     * 求指定天数后的时间
     *
     * @param date     指定的时间
     * @param increase 天
     * @return 时间
     */
    public static Date increaseDay2Date(Date date, int increase) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.DAY_OF_MONTH, increase);
        return cal.getTime();
    }

    /**
     * 求指定月份数后的时间
     *
     * @param date     日期时间
     * @param increase 月数
     * @return 时间
     */
    public static LocalDateTime localDateTimeAddMonth(LocalDateTime date, int increase) {
        return date.plusMonths(increase);
    }

    /**
     * 把字符串日期转换成默认格式的Date
     *
     * @param strDate 字符串日期
     * @param format  指定格式
     * @return 时间对象
     */
    public static Date format(String strDate, String format) {
        Date date;
        if (StringUtils.isBlank(strDate)) {
            return null;
        }
        try {
            date = getFormatter(format).parse(strDate);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }

    /**
     * 将字符串日期格式化对象
     *
     * @param format 指定格式
     * @return 日期格式化对象
     */
    public static SimpleDateFormat getFormatter(String format) {
        return new SimpleDateFormat(format);
    }

    /**
     * 获取month所在月的所有天
     *
     * @param month      要查询的日期（如果为null 则默认为当前月）
     * @param dateFormat 返回日期的格式（如果为null 则返回默认格式结果）
     * @return 天List
     */
    public static List<String> getAllDaysOfMonthInString(Date month, DateFormat dateFormat) {
        List<String> rs = new ArrayList<>();
        DateFormat df = null;
        if (null == dateFormat) {
            df = new SimpleDateFormat(YYYY_MM_DD_FORMAT);
        }
        Calendar cad = Calendar.getInstance();
        if (null != month) {
            cad.setTime(month);
        }
        // 获取当月天数
        int dayMonth = cad.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < dayMonth; i++) {
            cad.set(Calendar.DAY_OF_MONTH, i + 1);
            if (df != null) {
                rs.add(df.format(cad.getTime()));
            }
        }
        return rs;
    }

    /**
     * 获取month所在月的所有日期
     *
     * @param month 要查询的日期（如果为null 则默认为当前月）
     * @return 日期List
     */
    public static List<Date> getAllDaysOfMonth(Date month) {
        List<Date> rs = new ArrayList<>();
        Calendar cad = Calendar.getInstance();
        if (null != month) {
            cad.setTime(month);
        }
        // 获取当月天数
        int dayMonth = cad.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < dayMonth; i++) {
            cad.set(Calendar.DAY_OF_MONTH, i + 1);
            rs.add(cad.getTime());
        }
        return rs;
    }

    /**
     * 获取指定日期区间所有天
     *
     * @param begin      开始日期
     * @param end        结束日期
     * @param dateFormat (如果为null 则返回yyyy-MM-dd格式的日期)
     * @return 天List
     */
    public static List<String> getSpecifyDaysOfMonthInString(Date begin, Date end, DateFormat dateFormat) {
        DateFormat df = null;
        if (null == dateFormat) {
            df = new SimpleDateFormat(YYYY_MM_DD_FORMAT);
        }
        List<String> rs = new ArrayList<>();
        List<Date> tmplist = getSpecifyDaysOfMonth(begin, end);
        for (Date date : tmplist) {
            if (df != null) {
                rs.add(df.format(date));
            }
        }
        return rs;
    }

    /**
     * 获取指定日期区间所有日期
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 日期List
     */
    public static List<Date> getSpecifyDaysOfMonth(Date begin, Date end) {
        List<Date> rs = new ArrayList<>();
        Calendar cad = Calendar.getInstance();
        int dayMonth;
        // 设置开始日期为指定日期
        if (null == begin) {
            // 设置开始日期为当前月的第一天
            cad.set(Calendar.DAY_OF_MONTH, 1);
            begin = cad.getTime();
        }
        cad.setTime(begin);
        // 如果结束日期为空 ，设置结束日期为下月的第一天
        if (null == end) {
            // 获取当月天数
            dayMonth = cad.getActualMaximum(Calendar.DAY_OF_MONTH);
            cad.set(Calendar.DAY_OF_MONTH, dayMonth + 1);
            end = cad.getTime();
        }
        // 设置开始日期为当前月的第一天
        cad.set(Calendar.DAY_OF_MONTH, 1);
        Date tmp;
        int i = 1;
        while (true) {
            cad.set(Calendar.DAY_OF_MONTH, i);
            i++;
            tmp = cad.getTime();
            if (tmp.before(end)) {
                rs.add(cad.getTime());
            } else {
                break;
            }
        }
        return rs;
    }

    /**
     * 获取当前日期
     *
     * @return 一个包含年月日的<code>Date</code>型日期
     */
    public static synchronized Date getCurrDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * 将日期类型转换成指定字符串格式
     *
     * @param date    日期
     * @param pattern 字符串格式
     * @return 日期字符串
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 获取当前完整时间,样式: yyyy－MM－dd hh:mm:ss
     *
     * @return 一个包含年月日时分秒的<code>String</code>型日期。yyyy-MM-dd hh:mm:ss
     */
    public static String getCurrDateTimeStr() {
        return format(getCurrDate(), YYYY_MM_DD_HH_MM_SS_FORMAT);
    }

    /**
     * 获得指定日期的前一天
     *
     * @param specifiedDay YYYY_MM_DD_HH_MM_SS 格式
     * @param formatStr    日期类型
     * @return 日期
     */
    public static String getSpecifiedDayBefore(String specifiedDay, String formatStr) {
        Calendar c = Calendar.getInstance();
        Date date;
        try {
            date = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_FORMAT).parse(specifiedDay);
        } catch (ParseException e) {
            return null;
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
        return format(c.getTime(), formatStr);
    }

    /**
     * 获得指定日期的后一天
     *
     * @param specifiedDay YYYY_MM_DD_HH_MM_SS 格式
     * @param formatStr    日期类型
     * @return 日期
     */
    public static String getSpecifiedDayAfter(String specifiedDay, String formatStr) {
        Calendar c = Calendar.getInstance();
        Date date;
        try {
            date = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS_FORMAT).parse(specifiedDay);
        } catch (ParseException e) {
            return null;
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);
        return format(c.getTime(), formatStr);
    }

    /**
     * 获取本周第一天的日期
     *
     * @return 日期
     */
    public static String getWeekFirstDay() {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_FORMAT);
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 2;
        cal.add(Calendar.DATE, -dayOfWeek);
        return sdf.format(cal.getTime());
    }

    /**
     * 获取当前月的第一天
     *
     * @return 日期
     */
    public static String getCurrentMonthFirstDay() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_FORMAT);
        // 当前月的第一天
        cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
        Date beginTime = cal.getTime();
        return sdf.format(beginTime);
    }

    /**
     * 获取昨天开始时间
     *
     * @return 日期
     */
    public static String getYesterdayStart() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_FORMAT);
        return sdf.format(cal.getTime());
    }

    /**
     * 获取昨天结束时间
     *
     * @return 日期
     */
    public static String getYesterdayEnd() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_FORMAT);
        return sdf.format(cal.getTime()) + " 23:59:59";
    }

    /**
     * 获取当前开始时间
     *
     * @return 日期
     */
    public static String getCurrDayStart() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_FORMAT);
        return sdf.format(cal.getTime());
    }

    /**
     * 功能：获取指定月份的第一天<br/>
     */
    public static String getStartDayWithMonth(String month) throws ParseException {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_FORMAT);
        SimpleDateFormat mf = new SimpleDateFormat(YYYY_MM_FORMAT);
        Date date = mf.parse(month);
        calendar.setTime(date);
        // 因为格式化时默认了DATE为本月第一天所以此处为0
        calendar.add(Calendar.DATE, 0);
        return sdf.format(calendar.getTime());
    }

    /**
     * 功能：获取指定月份的最后一天<br/>
     */
    public static String getEndDayWithMonth(String month) throws ParseException {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_FORMAT);
        SimpleDateFormat mf = new SimpleDateFormat(YYYY_MM_FORMAT);
        Date date = mf.parse(month);
        calendar.setTime(date);
        // api解释roll()：向指定日历字段添加指定（有符号的）时间量，不更改更大的字段
        calendar.roll(Calendar.DATE, -1);
        return sdf.format(calendar.getTime());
    }

    public static String formatYearMonthDay(String dateStr) throws ParseException {
        if (StringUtils.isNotBlank(dateStr)) {
            SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_FORMAT);
            Date date = sdf.parse(dateStr);
            return sdf.format(date);
        } else {
            return "";
        }
    }

    /**
     * 功能：<br/>
     * 根据时间 yyyy-MM-dd 获取该日期是本月第几周
     */
    public static int getWeekIndexOfMonth(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_FORMAT);
        Date date = sdf.parse(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获取当前时间到指定时间距离多少秒 功能：<br/>
     */
    public static int getSecondToDesignationTime(String designationTime) {
        // 24小时制
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date toDate;
        try {
            toDate = dateFormat.parse(designationTime);
            return (int) ((toDate.getTime() - dateFormat.parse(DateUtil.getCurrDateTimeStr()).getTime()) / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前时间的年份
     *
     * @return 年
     */
    public static int getYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取当前时间的月份
     *
     * @return 月
     */
    public static int getMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前时间的天
     *
     * @return 天
     */
    public static int getDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 时间间隔
     */
    public static int differentDaysByMillisecond(LocalDateTime start, LocalDateTime end) {
        // ZoneOffset.of("+8") 是指定为东8区
        return (int) ((end.toInstant(ZoneOffset.of("+8")).toEpochMilli()
                - start.toInstant(ZoneOffset.of("+8")).toEpochMilli()) / (1000 * 3600 * 24));
    }
}

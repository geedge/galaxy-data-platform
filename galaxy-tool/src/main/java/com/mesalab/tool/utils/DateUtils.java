package com.mesalab.tool.utils;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>时间/日期处理工具类</p>
 * @author 中电积至有限公司 darnell
 * @version 1.0 创建时间：2010-11-8 下午04:48:57
 */
public class DateUtils implements TimeConstants {

    /**
     *<p>Description:抑制默认的构造器,避免实例化对象 </p>
     */
    private DateUtils() {

    }

    /** <p>获得当前日期，格式为默认yyyy-MM-dd.</p>
            * <pre>
    * DateUtil.getCurrentDate() = "2010-11-02"
            * </pre>
            * @see #getCurrentDate(String)
    * @return String    返回标准日期格式(yyyy-MM-dd)
    */
    public static String getCurrentDate() {

        DateTime dateTime = DateTime.now();

        return dateTime.toString(TimeConstants.YYYY_MM_DD);
    }


    /**
     *
     * <p>
     * 获得当前时间.
     * 如果pattern为空,将抛出{@link NullPointerException}
     * </p>
     * <pre>
     * DateUtil.getCurrentDate(TimeConstants.YYYY_MM_DD) = "2010-11-02"
     * DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss") = "2010-11-02 17:20:59"
     * </pre>
     * @param pattern 描述日期和时间格式.
     * @return 当前日期
     *
     */
    public static String getCurrentDate(String pattern) {

        if(StringUtil.isEmpty(pattern)){
            throw new NullPointerException("未规定时间格式...");
        }
        DateTime dateTime = DateTime.now();
        return dateTime.toString(pattern);
    }


    /**
     *
     * <p> 获得昨天时间.通过指定日期和时间格式,具体可参考本类提供的时间格式常量.</p>
     * <pre>
     * DateUtil.getDateOfYesterday(TimeConstants.YYYY_MM_DD_HH24_MM_SS) = "2010-11-04 16:59:13"
     * </pre>
     * @param pattern  描述日期和时间格式
     * @see #getFormatDate(Date, String)
     * @return String    返回日期的字符串表现形式值.
     */
    public static String getDateOfYesterday(String pattern) {
        DateTime dateTime = new DateTime();
        DateTime yesterday = dateTime.minusDays(1);
        return yesterday.toString(pattern);

    }



    /**
     *
     * <p>
     * 得到距离当天<code>offset</code>天数的某天日期，通过 <code>offset</code> 的值（+/-）来确定增减计算.
     * </p>
     * <pre>
     * 当天日期： 2010-11-05
     * DateUtil.getSomeDate("yyyy-MM-dd", 2) = 2010-11-07
     * DateUtil.getSomeDate("yyyy-MM-dd", -2) = 2010-11-03
     * DateUtil.getSomeDate("yyyy-MM-dd", 0) = 2010-11-05
     * </pre>
     * @param pattern 描述日期和时间格式
     * @param offset 描述增加\减少几日,为0时为当前日期.
     * @see #getFormatDate(Date, String)
     * @return  返回日期格式的字符串
     */

    public static String getSomeDate(String pattern, int offset) {
        DateTime dateTime = new DateTime();
        DateTime someDate = dateTime.withFieldAdded(DurationFieldType.days(), offset);
        return someDate.toString(pattern);
    }

    /**
     *
     * <p>
     * 得到距离指定时间<code>offset</code>天数的时间，通过 <code>offset</code> 的值（+/-）来确定增减计算.
     * </p>
     * <pre>
     * 当天日期： 2010-11-05
     * DateUtil.getSomeDate("yyyy-MM-dd", 2) = 2010-11-07
     * DateUtil.getSomeDate("yyyy-MM-dd", -2) = 2010-11-03
     * DateUtil.getSomeDate("yyyy-MM-dd", 0) = 2010-11-05
     * </pre>
     * @param  date 时间
     * @param offset 描述增加\减少几日,为0时为当前日期.
     * @return  返回日期格式的字符串
     */

    public static Date getSomeDate(Date date, int offset) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(offset).toDate();
    }



    /**
     *
     * <p>
     * 得到距离当前时间<code>offset</code>小时的时间，通过 <code>offset</code> 的值（+/-）来确定增减计算.
     * </p>
     * <pre>
     * 当天时间： 2018-11-05 11:13:22
     * DateUtil.getSomeHour(date, 2) =  2018-11-05 13:13:22
     * DateUtil.getSomeHour(date, -2) = 2018-11-05 09:13:22
     * DateUtil.getSomeHour(date, 0) = 2018-11-05 11:13:22
     * </pre>
     * @param date 时间
     * @param offset 描述增加\减少小时数,为0时为当前日期.
     * @return  返回距离当前时间小时数的时间
     */
    public static Date getSomeHour(Date date, int offset) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusHours(offset).toDate();
    }



    /**
     *
     * <p>
     * 得到距离当前时间<code>offset</code>分钟数的时间，通过 <code>offset</code> 的值（+/-）来确定增减计算.
     * </p>
     * <pre>
     * 当天时间： 2018-11-05 11:13:22
     * DateUtil.getSomeMinute(date, 2) =  2018-11-05 11:15:22
     * DateUtil.getSomeMinute(date, -2) = 2018-11-05 11:11:22
     * DateUtil.getSomeMinute(date, 0) = 2018-11-05 11:13:22
     * </pre>
     * @param date 时间
     * @param offset 描述增加\减少分钟数,为0时为当前日期.
     * @return  返回距离当前时间分钟数的时间
     */
    public static Date getSomeMinute(Date date, int offset) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(offset).toDate();
    }


    /**
     *
     * <p>
     * 得到距离当前时间格式化分钟后的时间，5分钟倍数
     * </p>
     * <pre>
     * 当天时间： 2018-11-05 11:13:22
     * DateUtil.getFormatMinute(date) =  2018-11-05 11:10:00
     * </pre>
     * @param date 时间
     * @return  返回距离当前时间5分钟倍数的时间
     */
    public static Date getGranularityFiveMinute(Date date) {
        return getTimeFloor(date, "PT5M");
    }



    /**
     *
     * <p>
     * 得到距离当前时间<code>offset</code>秒数的时间，通过 <code>offset</code> 的值（+/-）来确定增减计算.
     * </p>
     * <pre>
     * 当天时间： 2018-11-05 11:13:22
     * DateUtil.getSomeSecond(date, 2) =  2018-11-05 11:13:24
     * DateUtil.getSomeSecond(date, -2) = 2018-11-05 11:13:20
     * DateUtil.getSomeSecond(date, 0) = 2018-11-05 11:13:22
     * </pre>
     * @param date 时间
     * @param offset 描述增加\减少秒数,为0时为当前日期.
     * @return  返回距离当前时间秒数的时间
     */
    public static Date getSomeSecond(Date date, int offset) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(offset).toDate();
    }



    /**
     *
     * <p>获得指定日期当前周第一天</p>
     * <pre>
     *  当前时间 2018-12-26 11:47:00
     * DateUtil.getFirstDayOfWeek(new Date(),TimeConstants.YYYY_MM_DD_HH24_MM_SS) = "2018-12-24 00:00:00"
     * </pre>
     * @param date 指定日期.
     * @param pattern  描述日期和时间格式
     * @return 返回日期格式的字符串
     */
    public static String getFirstDayOfWeek(Date date, String pattern) {

        if (StringUtil.isEmpty(pattern) || StringUtil.isEmpty(date)) {
            throw new NullPointerException("时间不能为空或解析格式不能为空");
        }

        DateTime firstDay = DateTime.parse(getFormatDate(date, TimeConstants.YYYY_MM_DD))
                .dayOfWeek().setCopy(1).toDateTime();

        return firstDay.toString(pattern);
    }


    /**
     *
     * <p>获得指定日期当前周最后一天</p>
     * <pre>
     *  当前时间 2018-12-26 11:47:00
     * DateUtil.getFirstDayOfWeek(new Date(),TimeConstants.YYYY_MM_DD_HH24_MM_SS) = "2018-12-30 23:59:59"
     * </pre>
     * @param date 指定日期.
     * @param pattern  描述日期和时间格式
     * @return 返回日期格式的字符串
     */
    public static String getLastDayOfWeek(Date date, String pattern) {

        if (StringUtil.isEmpty(pattern) || StringUtil.isEmpty(date)) {
            throw new NullPointerException("时间不能为空或解析格式不能为空");
        }

        DateTime lastDay =  new DateTime(date)
                .dayOfWeek().withMaximumValue().millisOfDay().withMaximumValue().toDateTime();

        return lastDay.toString(pattern);
    }






    /**
     *
     * <p>获得指定日期当月第一天</p>
     * <pre>
     * DateUtil.getFirstDayOfMonth(new Date(), TimeConstants.YYYY_MM_DD_HH24_MM_SS) = "2010-11-01 00:00:00"
     * </pre>
     * @param date 指定日期.
     * @param pattern  描述日期和时间格式
     * @return 返回日期格式的字符串
     */
    public static String getFirstDayOfMonth(Date date, String pattern) {

        if (StringUtil.isEmpty(pattern) || StringUtil.isEmpty(date)) {
            throw new NullPointerException("时间不能为空或解析格式不能为空");
        }

        DateTime firstDay = DateTime.parse(getFormatDate(date, TimeConstants.YYYY_MM_DD))
                        .dayOfMonth().setCopy(1).toDateTime();

        return firstDay.toString(pattern);
    }


    /**
     *
     * <p>获得指定日期当月最后一天</p>
     * <pre>
     * DateUtil.getLastDayOfMonth(new Date(), TimeConstants.YYYY_MM_DD_HH24_MM_SS) = "2010-11-30 23:59:59"
     * </pre>
     * @param date 指定日期
     * @param pattern  描述日期和时间格式
     * @return 返回日期格式的字符串
     */
    public static String getLastDayOfMonth(Date date,
                                           String pattern) {

        if (StringUtil.isEmpty(pattern) || StringUtil.isEmpty(date)) {
            throw new NullPointerException("时间不能为空或解析格式不能为空");
        }

        DateTime lastDay =  new DateTime(date)
                    .dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue().toDateTime();

        return lastDay.toString(pattern);
    }





    /**
     *
     * <p>获得指定日期的日.</p>
     * @param date 日期值
     * @return 返回日期的日.如果{@link java.util.Date}为<code>null</code>,抛出异常.
     * @throws NullPointerException
     */
    public static String getDDFromDate(Date date) {
        if (StringUtil.isEmpty(date)) {
            throw new NullPointerException("date 不能为空");
        }

        return String.valueOf(new DateTime(date).getDayOfMonth());

    }


    /**
     *
     * <p>获得指定日期的月.</p>
     * @param date 指定日期
     * @return 返回日期的月.如果{@link java.util.Date}为<code>null</code>,抛出异常.
     * @throws NullPointerException
     */
    public static String getMMFromDate(Date date) {

        if (StringUtil.isEmpty(date)) {
            throw new NullPointerException("date 不能为空");
        }
        return String.valueOf(new DateTime(date).getMonthOfYear());

    }


    /**
     *
     * <p>获得指定日期的年.</p>
     * @param date 指定日期
     * @return 返回日期的年.如果{@link java.util.Date}为<code>null</code>,抛出异常.
     * @throws NullPointerException
     */
    public static String getYYFromDate(Date date) {

        if (StringUtil.isEmpty(date)) {
            throw new NullPointerException("date 不能为空");
        }
        return String.valueOf(new DateTime(date).getYear());

    }





    /**
     *
     * <p>返回两个日期之间的所有天数,放入集合{@link List}中.</p>
     * <pre>
     * DateUtil.getDateList(DateUtil.convertStringToDate(DateUtil.DATE_PATTERN_YYYY_MM_DD,
     *				"2010-10-22"),new Date()) =  list
     * </pre>
     * @param begin  开始时间
     * @param end 结束时间
     * @return 返回两个日期相隔时间集合
     * @throws NullPointerException
     */
    public static List<Date> getEveryDay(Date begin, Date end) {

        if (StringUtil.isEmpty(begin) || StringUtil.isEmpty(end)) {
            throw new NullPointerException("date 不能为空");
        }
        List<Date> result = new ArrayList<Date>();
        DateTime beginDate = new DateTime(begin);
        DateTime endDate = new DateTime(end);

        while (endDate.isAfter(beginDate)) {
            result.add(beginDate.toDate());
            beginDate = beginDate.plusDays(1);
        }




        return result;

    }


    /**
     * 以基准日期为起点向左获取所有时间点列表。
     * @param benchmarkDate 基准时间
     * @param endDate  终止时间，用于定义时间数据范围
     * @param period 参见ISO8601 对durations定义，例如PT5M,P1W; 约束不可为复合周期(P1YT5M)
     * @return
     */
    public static List<Date> getLeftDateRange(Date benchmarkDate, Date endDate, String period) {

        if (StringUtil.isEmpty(endDate) || StringUtil.isEmpty(benchmarkDate)) {
            throw new NullPointerException("benchmarkDate and endDate is not null.");
        }

        List<Date> dateList = Lists.newArrayList();

        Period granularityPeriod = Period.parse(period);
        DateTime benchmarkTime =  new DateTime (benchmarkDate);
        DateTime endTime = new DateTime((endDate));
        while(benchmarkTime.compareTo(endTime) > 0) {
            benchmarkTime = benchmarkTime.minus(granularityPeriod);
            dateList.add(benchmarkTime.toDate());
        }

        return dateList;

    }

    /**
     * 以基准日期为起点向右获取所有时间点列表。
     * @param benchmarkDate 基准时间
     * @param endDate  终止时间，用于定义时间数据范围
     * @param period 参见ISO8601 对durations定义，例如PT5M,P1W; 约束不可为复合周期(P1YT5M)
     * @return
     */
    public static List<Date> getRightDateRange(Date benchmarkDate, Date endDate, String period) {

        if (StringUtil.isEmpty(endDate) || StringUtil.isEmpty(benchmarkDate)) {
            throw new NullPointerException("benchmarkDate and endDate is not null.");
        }

        List<Date> dateList = Lists.newArrayList();

        Period granularityPeriod = Period.parse(period);
        DateTime benchmarkTime =  new DateTime (benchmarkDate);
        DateTime endTime = new DateTime((endDate));
        while(benchmarkTime.compareTo(endTime) <= 0) {
            dateList.add(benchmarkTime.toDate());
            benchmarkTime = benchmarkTime.plus(granularityPeriod);
        }

        return dateList;

    }

    /**
     * 按照时间粒度，获取指定时间范围内的所有时间点。
     * 例如：2019-10-11 10:00:00/2019-10-11 10:20:00,时间周期每5分钟（PT5M)，
     * 则获取结果为[2019-10-11 10:00:00,2019-10-11 10:05:00,2019-10-11 10:10:00,2019-10-11 10:15:00,2019-10-11 10:20:00]
     * @param startDate 起始时间
     * @param endDate 终止时间
     * @param period 时间粒度 参见ISO8601 对durations定义，例如PT5M,P1W; 约束不可为复合周期(P1YT5M)
     * @return
     */
    public static List<Date> getDateRange(Date startDate, Date endDate, String period) {

        if (StringUtil.isEmpty(startDate) || StringUtil.isEmpty(endDate)) {
            throw new NullPointerException("startDate and endDate is not null.");
        }


        List<Date> dateList = Lists.newArrayList();

        Period granularityPeriod = Period.parse(period);

        DateTime floorStartDate = new DateTime(getTimeFloor(startDate, period));

        DateTime endDateTime = new DateTime(endDate);


        while(floorStartDate.compareTo(endDateTime) <= 0) {
            dateList.add(floorStartDate.toDate());
            floorStartDate = floorStartDate.plus(granularityPeriod);
        }


        return dateList;
    }






    /**
     *
     * <p>根据两个日期获得之间所有的工作日期返回一个日期数组 (工作日不包括星期六和星期日)</p>
     * <pre>
     * DateUtil.getWorkingdayList(DateUtil.convertStringToDate(TimeConstants.YYYY_MM_DD,
     "2010-10-15"),new Date()) = list
     * </pre>
     * @param begin 开始日期
     * @param end 结束日期
     * @return 返回两个日期相隔时间集合
     * @throws NullPointerException
     */
    public static List<Date> getWorkingdayList(Date begin,
                                               Date end) {
        if (null == begin || null == end ) {
            throw new NullPointerException();
        }
        List<Date> list = getEveryDay(begin, end);
        List<Date> result = new ArrayList<Date>();
        for (int i = 0; i < list.size(); i++) {

            if (new DateTime(list.get(i)).getDayOfWeek() == 6
                    || new DateTime(list.get(i)).getDayOfWeek() == 7) {
                continue;
            } else {
                result.add(list.get(i));
            }
        }

        return result;
    }


    /**
     *
     * <p>根据日期类型的数组获得数组内所有日期对应的星期X列表</p>
     * @param list 日期类型的数组
     * @return  星期X列表
     */
    public static List<String> getListOfDayOfWeek(List<Date> list) {

        List<String> result = new ArrayList<String>();

        for (Date date:list) {
           DateTime dateTime = new DateTime(date);

            switch (dateTime.getDayOfWeek()) {

                case DateTimeConstants.SUNDAY:
                    result.add("星期日");
                    break;
                case DateTimeConstants.MONDAY:
                    result.add("星期一");
                    break;
                case DateTimeConstants.TUESDAY:
                    result.add("星期二");
                    break;
                case DateTimeConstants.WEDNESDAY:
                    result.add("星期三");
                    break;
                case DateTimeConstants.THURSDAY:
                    result.add("星期四");
                    break;
                case DateTimeConstants.FRIDAY:
                    result.add("星期五");
                    break;
                case DateTimeConstants.SATURDAY:
                    result.add("星期六");
                    break;
                default:
                    break;
            }

        }
        return result;
    }


    /**
     *
     * <p>将日期,转换成10进制日期</p>
     * <p>
     * pattern需跟date格式相同,才可进行转换.转换后的长整型.
     * </p>
     * <pre>
     * DateUtil.convertStringToTimestamp(TimeConstants.YYYY_MM_DD, "2010-10-11") = 1286726400
     * </pre>
     * @param date 时间值
     * @param pattern 描述日期与格式
     * @throws NullPointerException
     * @see #convertTimestampToString(long, String)
     * @return 返回10进制时间值字符串
     */
    public static long convertStringToTimestamp(String date,  String pattern) {

        if(StringUtil.isEmpty(date) || StringUtil.isEmpty(pattern)) {
            throw new NullPointerException("date 与 pattern 不能为空");
        }

        DateTimeFormatter format = DateTimeFormat.forPattern(pattern);
        return  DateTime.parse(date, format).toDateTime().getMillis() / 1000;

    }



    /**
     *
     * <p>将10进制,转换日期字符串类型.</p>
     * <pre>
     * DateUtil.convertTimestampToString(TimeConstants.YYYY_MM_DD, 1286726400l) = "2010-10-11 00:00:00"
     * </pre>
     * @param time10 时间戳值
     * @param  pattern 描述日期与格式
     * @see #getFormatDate(Date, String)
     * @see #convertStringToTimestamp(String, String)
     * @return 返回日期字符串
     * @throws NullPointerException
     */
    public static String convertTimestampToString(long time10, String pattern) {

        if(StringUtil.isBlank(pattern)) {
            throw new NullPointerException("pattern 不能为空");
        }

       return   DateTime.now().withMillis(time10 * 1000).toString(pattern);
    }







    /**
     *
     * <p>
     *  将日期转变为字符串的表现形式.
     * </p>
     * <p>
     * 日期格式可以参考本类提供的日期常量,也可自己定义日期格式.
     * 当<code>pattern</code> 或 <code>date</code> 为空时,抛出空指针异常.
     * </p>
     * <pre>
     * DateUtil.getFormatDate("yyyyMMddHHmmss", new Date()) = "20101102174448"
     * </pre>
     * @param date 解析的日期值
     * @param pattern 描述日期与时间格式
     * @return  返回日期的字符串表现形式.
     * @throws NullPointerException
     */
    public static String getFormatDate(Date date , String pattern) {

        if(StringUtil.isEmpty(pattern) || StringUtil.isEmpty(date)){
            throw new NullPointerException("参数为NULL......");
        }
        DateTime  dateTime = new DateTime(date);

        return dateTime.toString(pattern);
    }

    /**
     *
     * <p>通过字符串日期类型,转换为Date类型日期.</p>
     * <p>
     * pattern格式，需跟提供的日期字符串格式相统一，否则解析异常返回NULL值。
     * </P>
     * <pre>
     * DateUtil.convertStringToDate(TimeConstants.YYYY_MM_DD, "2010-09-11") = date类型日期
     * DateUtil.convertStringToDate(TimeConstants.YYYY_MM_DD_HH_MM, "2010-09-11") = NULL.
     * </pre>
     * @param date 字符串类型日期.
     * @param pattern 描述日期与时间格式.
     * @return 返回解析后的{@link java.util.Date }类型日期.
     * @throws NullPointerException
     */
    public static Date convertStringToDate(String date, String pattern) {


        if (StringUtil.isEmpty(date) || StringUtil.isEmpty(pattern)) {
            throw new NullPointerException("date 与 pattern 不能为空");
        }

        DateTimeFormatter format = DateTimeFormat.forPattern(pattern);

        return DateTime.parse(date, format).toDate();
    }

    /**
     * 对当前时间根据粒度往下舍入时间,返回一个新的时间
     * 例如 getTimeFloor("2019-12-12 12:32:12", "PT5M") = "2019-12-12 12:30:00"
     * @param date
     * @param periodGranularity
     * @return
     */
    public static Date getTimeFloor(Date date, String periodGranularity) {

        Period period = Period.parse(periodGranularity);
        if (isCompoundPeriod(period)) {
            throw  new IllegalArgumentException("periodGranularity is not supported:"+ period.toString());
        }

        DateTime dateTime = new DateTime(date);

        final int years = period.getYears();
        if (years > 0) {
            int y = dateTime.getYear();
            y -= y % years;
            dateTime = dateTime.withYear(y).withMonthOfYear(1).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

            return dateTime.toDate();
        }

        final int months = period.getMonths();
        if (months > 0) {

            int m = dateTime.getMonthOfYear();
            if (m % months != m) {
                m -= m % months;
            }
            dateTime = dateTime.withMonthOfYear(m).withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
            return dateTime.toDate();
        }


        final int weeks = period.getWeeks();
        if (weeks > 0) {
            int w = dateTime.getWeekOfWeekyear();

            if (w % weeks != w) {
                w -= w % weeks;
            }
            dateTime = dateTime.withWeekOfWeekyear(w).withDayOfWeek(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

            return dateTime.toDate();
        }



        final int days = period.getDays();
        if (days > 0) {
            int d = dateTime.getDayOfMonth();
            if (d % days != d) {
                d -= d % days;
            }

            dateTime = dateTime.withDayOfMonth(d).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
            return dateTime.toDate();
        }


        final int hours = period.getHours();
        if (hours > 0) {
            int h = dateTime.getHourOfDay();
            h -= h % hours;
            dateTime = dateTime.withHourOfDay(h).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
            return dateTime.toDate();
        }



        final int minutes = period.getMinutes();
        if (minutes > 0) {

            int m = dateTime.getMinuteOfHour();
            m -= m % minutes;

            dateTime = dateTime.withMinuteOfHour(m).withSecondOfMinute(0).withMillisOfSecond(0);

            return dateTime.toDate();

        }

        final int seconds = period.getSeconds();
        if (seconds > 0) {

            int s = dateTime.getSecondOfMinute();
            s -= s % seconds;

            dateTime = dateTime.withSecondOfMinute(s).withMillisOfSecond(0);

            return dateTime.toDate();

        }


        return null;




    }






    /**
     * 判定周期是否为复合周期，用于时序数据粒度的判定
     * 例如:按照ISO_8601 规定Durations定义, P1DT12H 为复合周期，P1D 为单个周期
     * @param period
     * @return
     */
    private static boolean isCompoundPeriod(Period period) {
        int[] values = period.getValues();
        boolean single =  false;
        for (int value : values) {
            if (value > 0) {
                if(single) {
                    return true;
                }

                single = true;

            }
        }

        return false;
    }




}

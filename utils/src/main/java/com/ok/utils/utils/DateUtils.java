package com.ok.utils.utils;

import android.text.TextUtils;

import com.ok.utils.utils.Utils;
import com.ok.utils.utils.log.LogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 * <p/>
 * <p>One hundred thousand</p>
 *
 * @description 日期、时间工具类
 *
 * @author li zhaoxiong
 * @modify
 * @version 1.0.0
 */
public class DateUtils {
	public static final int MIN = 60;
	public static final int HOUR = MIN * 60;
	public static final int DAY = HOUR * 24;

	/**
	 * 得到当前几号
	 * @return
	 */
	public static String getCurrentDay() {
		return getFormatCurrentTime("dd");
	}

	/**
	 * 得到当前月份
	 * @return
	 */
	public static String getCurrentMonth() {
		return getFormatCurrentTime("M");
	}

	/**
	 * 得到当前年份
	 * @return
	 */
	public static String getCurrentYear() {
		return getFormatCurrentTime("yyyy");
	}

	/**
	 * 得到当前时间yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getCurrentTime() {
		return getFormatDateTime(new Date(), "yyyy-M-dd HH:mm:ss");
	}

	/**
	 * 得到星期几
	 * (格式"yyyy-MM-dd"或者"yyyy/MM/dd")
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(String date) {
		String[] temp = null;
		if (date.indexOf("/") > 0) {
			temp = date.split("/");
		}
		if (date.indexOf("-") > 0) {
			temp = date.split("-");
		}
		return getDayOfWeek(temp[0], temp[1], temp[2]);
	}

	/**
	 * 得到当前月有多少天
	 * @return
	 */
	public static int getDaysOfCurMonth() {
		int curyear = Integer.valueOf(getCurrentYear()).intValue();
		int curMonth = Integer.valueOf(getCurrentMonth()).intValue();
		int mArray[] = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if ((curyear % 400 == 0) || ((curyear % 100 != 0) && (curyear % 4 == 0))) {
			mArray[1] = 29;
		}
		return mArray[curMonth - 1];
	}

	/**
	 * 得到下个月有多少天
	 * yyyy-MM-dd或者yyyy/MM//dd格式
	 * @return
	 */
	public static int getNextMonthDays(String argDate) {
		String[] temp = null;
		if (argDate.indexOf("/") > 0) {
			temp = argDate.split("/");
		}
		if (argDate.indexOf("-") > 0) {
			temp = argDate.split("-");
		}
		Calendar cal = new GregorianCalendar(Integer.valueOf(temp[0]).intValue(), Integer.valueOf(temp[1]).intValue() - 1, Integer.valueOf(temp[2])
				.intValue());
		int curMonth = cal.get(Calendar.MONTH);
		cal.set(Calendar.MONTH, curMonth + 1);
		int curyear = cal.get(Calendar.YEAR);
		curMonth = cal.get(Calendar.MONTH);
		int mArray[] = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if ((curyear % 400 == 0) || ((curyear % 100 != 0) && (curyear % 4 == 0))) {
			mArray[1] = 29;
		}
		return mArray[curMonth];
	}

	/**
	 * long转换为"XX小时XX分钟XX秒"
	 * @param second
	 * @return
	 */
	public static String getHour(long second) {
		long hour = second / HOUR;
		long minute = (second - hour * HOUR) / MIN;
		long sec = (second - hour * HOUR) - minute * MIN;

		return hour + "小时" + minute + "分" + sec + "秒";

	}

	/**
	 * 格式化时间，time的单位为毫秒
	 * @param time
	 * @return
	 */
	public static String formatTimeLength(long time) {
		time /= 1000;// 转化为秒
		if (time > HOUR) {
			return String.format(Locale.getDefault(), "%02d:%02d:%02d", time / HOUR, time / MIN, time % MIN);
		}
		return String.format(Locale.getDefault(), "%02d:%02d", time / MIN, time % MIN);
	}

	/**
	 * 格式化自定义格式时间
	 * @param format
	 * @return
	 */
	public static String getFormatDateTime(String format) {
		SimpleDateFormat sdate = new SimpleDateFormat(format, Locale.getDefault());
		return sdate.format(new Date());
	}

	public static String getFormatCurrentTime(String format) {
		return getFormatDateTime(new Date(), format);
	}

	public static String getFormatDateTime(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		return sdf.format(date);
	}

	public static int getDayOfWeek(String year, String month, String day) {
		Calendar cal = new GregorianCalendar(Integer.valueOf(year).intValue(), Integer.valueOf(month).intValue() - 1, Integer.valueOf(day).intValue());
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	public static String getChinaDayOfWeek(String date) {
		String[] weeks = new String[] { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		int week = getDayOfWeek(date);
		return weeks[week - 1];
	}

	/**
	 * 转换时间格式
	 * @param time
	 * @return
	 */
	public static String formatVisitTime(String time) {
		long timeInLong = StringUtil.parseStr2Long(time);
		return formatVisitTime(timeInLong);
	}

	/**
	 * 转换时间格式
	 * @param time
	 * @return
	 */
	public static String formatVisitTime(long time) {
//		if (time > 0) {
//			long now = System.currentTimeMillis();
//			long interval = (now - time) / 1000;
//			if (interval < MIN) {
//				return "刚刚";
//			}
//			else if (interval < HOUR) {
//				return String.format(Locale.getDefault(), "%d分钟前", (interval / MIN));
//			}
//			else if (interval < DAY) {
//				return String.format(Locale.getDefault(), "%d小时前", (interval / HOUR));
//			}
//			else {
//				int year = getYear(time);
//				int curYear = getYear(now);
//				if (year == curYear) {
//					return DateUtils.getDateOfMonthDay(time);
//				}
//				else {
//					return String.format(Locale.getDefault(), "%d年", year);
//				}
//			}
//		}
//		return "";
		if (time > 0) {
			long currentTime = System.currentTimeMillis();
			long timeInterval = (currentTime - time) / 1000;// 秒
			long today = getDateTime(getToDay());
			long yesterDay = getDateTime(getYesterDay());
			if (timeInterval < MIN) {
				return "刚刚更新 ";
			}
			else if (timeInterval < HOUR) {
				return timeInterval / MIN + "分钟前";
			}
			else if (time > today) {
				return getDateOfHour(time);
			}
			else if (time > yesterDay) {
				return "昨天" + getDateOfHour(time);
			}
			else if (time < getDateTime(getDateOfYear())) {
				return getDateOfMonthYear(time);
			}
			else {
				return getDateOfMonthDay(time);
			}
		}
		return "";
	}

	// 今天日期
	public static String getToDay() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd", Locale.getDefault());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		return formatter.format(calendar.getTime());
	}

	public static int getYear(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(time));
		int year = calendar.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 得到yyyy年MM月DD日
	 * 
	 * @param time
	 * @return
	 */
	public static String getDateOfMonthYear(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年M月dd日", Locale.getDefault());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return formatter.format(calendar.getTime());
	}

	/**
	 * 昨天日期 long to yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getYesterDay() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd", Locale.getDefault());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.DATE, -1);
		return formatter.format(calendar.getTime());
	}

	/**
	 * 得到小时 long to HH:mm
	 * 
	 * @return
	 */
	public static String getDateOfHour(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return formatter.format(calendar.getTime());
	}

	/**
	 * 得到年 long to yyyy
	 * 
	 * @return
	 */
	public static String getDateOfYear() {
		long time = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy", Locale.getDefault());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return formatter.format(calendar.getTime()) + "-00-00";
	}

	/**
	 * long to MM月dd日
	 * 
	 * @return
	 */
	public static String getDateOfMonthDay(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("M月dd日", Locale.getDefault());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return formatter.format(calendar.getTime());
	}

	/**
	 * 年月日变为long
	 * 
	 * @param time
	 * @return
	 */
	public static long getDateTime(String time) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd", Locale.getDefault());
		Date date;
		try {
			date = sdf.parse(time);
			long longDate = date.getTime();
			return longDate;
		}
		catch (ParseException e) {
			return 0;
		}
	}

	// 一年前日期
	public static String getLastYear() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd", Locale.getDefault());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.YEAR, -1);
		return formatter.format(calendar.getTime());
	}

	/**
	 * 毫秒变时间
	 * 
	 * @param time
	 * @return
	 */
	public static String toTime(int time) {
		time /= 1000;
		int minute = time / MIN;
//		int hour = minute / MIN;
		int second = time % MIN;
		minute %= MIN;
		return String.format(Locale.getDefault(), "%02d:%02d", minute, second);
	}

	public static boolean isLowerOneMinute(int time) {
		time /= 1000;
		int minute = time / MIN;
//		int hour = minute / MIN;
		LogUtils.println();
		minute %= MIN;
		if (minute < 1) {
			return true;
		}
		return false;
	}

	/**
	 * 时间转换yyyy-M-dd HH:mm:ss
	 * 
	 * @param time
	 * @return
	 */
	public static String convertTime(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd HH:mm:ss", Locale.getDefault());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);

		return formatter.format(calendar.getTime());
	}

	/**
	 * 时间转换yyyy-M-dd
	 * 
	 * @param time
	 * @return
	 */
	public static String convertTimeTwo(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd", Locale.getDefault());
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);

		return formatter.format(calendar.getTime());
	}

	/**
	 * 转换日期 将日期转为今天, 昨天, 前天, XXXX-XX-XX, ...
	 *
	 * @param time 时间
	 * @return 当前日期转换为更容易理解的方式
	 */
	public static String translateDate(Long time) {
		long oneDay = 24 * 60 * 60 * 1000;
		Calendar current = Calendar.getInstance();
		Calendar today = Calendar.getInstance();    //今天

		today.set(Calendar.YEAR, current.get(Calendar.YEAR));
		today.set(Calendar.MONTH, current.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
		//  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);

		long todayStartTime = today.getTimeInMillis();

		if (time >= todayStartTime && time < todayStartTime + oneDay) { // today
			return "今天";
		} else if (time >= todayStartTime - oneDay && time < todayStartTime) { // yesterday
			return "昨天";
		} else if (time >= todayStartTime - oneDay * 2 && time < todayStartTime - oneDay) { // the day before yesterday
			return "前天";
		} else if (time > todayStartTime + oneDay) { // future
			return "将来某一天";
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date(time);
			return dateFormat.format(date);
		}
	}

	/**
	 * 转换日期 转换为更为人性化的时间
	 *
	 * @param time 时间
	 * @return
	 */
	private String translateDate(long time, long curTime) {
		long oneDay = 24 * 60 * 60;
		Calendar today = Calendar.getInstance();    //今天
		today.setTimeInMillis(curTime * 1000);
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		long todayStartTime = today.getTimeInMillis() / 1000;
		if (time >= todayStartTime) {
			long d = curTime - time;
			if (d <= 60) {
				return "1分钟前";
			} else if (d <= 60 * 60) {
				long m = d / 60;
				if (m <= 0) {
					m = 1;
				}
				return m + "分钟前";
			} else {
				SimpleDateFormat dateFormat = new SimpleDateFormat("今天 HH:mm");
				Date date = new Date(time * 1000);
				String dateStr = dateFormat.format(date);
				if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
					dateStr = dateStr.replace(" 0", " ");
				}
				return dateStr;
			}
		} else {
			if (time < todayStartTime && time > todayStartTime - oneDay) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("昨天 HH:mm");
				Date date = new Date(time * 1000);
				String dateStr = dateFormat.format(date);
				if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {

					dateStr = dateStr.replace(" 0", " ");
				}
				return dateStr;
			} else  if (time < todayStartTime - oneDay && time > todayStartTime - 2 * oneDay){
				SimpleDateFormat dateFormat = new SimpleDateFormat("前天 HH:mm");
				Date date = new Date(time * 1000);
				String dateStr = dateFormat.format(date);
				if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
					dateStr = dateStr.replace(" 0", " ");
				}
				return dateStr;
			} else {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date date = new Date(time * 1000);
				String dateStr = dateFormat.format(date);
				if (!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
					dateStr = dateStr.replace(" 0", " ");
				}
				return dateStr;
			}
		}
	}
}

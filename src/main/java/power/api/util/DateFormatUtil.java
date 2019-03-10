package power.api.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
    final public static String YEAR_FORMAT = "yyyy";
    final public static String MONTH_FORMAT = "yyyy-MM";
    final public static String DAY_FORMAT = "yyyy-MM-dd";
    final public static String HOUR_FORMAT = "yyyy-MM-dd HH";
    final public static String MINUTE_FORMAT = "yyyy-MM-dd HH:mm";
    final public static String SECOND_FORMAT = "yyyy-MM-dd HH:mm:ss";
    final public static String SIMPLE_HOUR_FORMAT = "HH:mm:ss";
    final public static String SIMPLE_MINUTE_FORMAT = "HH:mm";
    final public static String SIMPLE_SECOND_FORMAT = "HH:mm:ss";

    public static String formatDateTo(Date date, String format) {
        return (new SimpleDateFormat(format)).format(date);
    }

    public static String formatDateTo(long millionSecond, String format) {
        return (new SimpleDateFormat(format)).format(new Date(millionSecond));
    }

    final public static String YEAR_FORMAT_SQL = "%Y";
    final public static String MONTH_FORMAT_SQL = "%Y-%m";
    final public static String DAY_FORMAT_SQL = "%Y-%m-%d";
    final public static String HOUR_FORMAT_SQL = "%Y-%m-%d %H";
    final public static String MINUTE_FORMAT_SQL = "%Y-%m-%d %H:%i";
    final public static String SECOND_FORMAT_SQL = "%Y-%m-%d %H:%i:%S";
}

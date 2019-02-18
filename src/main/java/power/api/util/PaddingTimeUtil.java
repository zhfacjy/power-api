package power.api.util;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PaddingTimeUtil {
    private static final String BASE_DATA_FORMAT = "yyyy-MM-dd";
    private static final String FULL_DATA_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static SimpleDateFormat simpleDateFormat = null;
    private static SimpleDateFormat fullFormat = null;

    static {
        simpleDateFormat = new SimpleDateFormat(BASE_DATA_FORMAT);
        fullFormat = new SimpleDateFormat(FULL_DATA_FORMAT);
    }

    private static <T> String getStringByMethodName(T o, String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return o.getClass().getDeclaredMethod(methodName).invoke(o).toString();
    }

    private static <T> T setStringByMethodName(T t, String methodName, String value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        t.getClass().getDeclaredMethod(methodName, String.class).invoke(t, value);
        return t;
    }

    private static String formatStringToDay(String dateString) {
        return dateString.substring(0, 10);
    }

    /**
     * 按照时间间隔补充一整天的时间序列
     * 用法参考：@/power/api/util/PaddingTimeUtilTest.java
     *
     * @param resultList 已经按照时间从小到大排好序的List数组对象
     * @param minute     相隔的分钟
     * @param methodName 获取和设置时间字符串的方法名，例如成员名为createAt，则传入CreateAt
     * @param classType  List对象中每个子项的类型
     * @param <T>
     * @return
     * @throws ParseException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> List<T> paddingZeroBy(List<T> resultList, int minute, String methodName, Class<T> classType) throws ParseException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        if (resultList.isEmpty()) {
            return resultList;
        }

        String getMethodName = "get" + methodName;
        String setMethodName = "set" + methodName;

        String minString = formatStringToDay(getStringByMethodName(resultList.get(0), getMethodName)) + " 00:00:00";
        String maxString = formatStringToDay(getStringByMethodName(resultList.get(resultList.size() - 1), getMethodName)) + " 23:00:00";
        long minTimestamp = fullFormat.parse(minString).getTime();
        long maxTimestamp = fullFormat.parse(maxString).getTime();
        long currentTimestamp = minTimestamp;
        int interval = minute * 60000;
        LinkedList<T> newDtoList = new LinkedList<>();
        while (currentTimestamp <= maxTimestamp) {
            if (!resultList.isEmpty()
                    && currentTimestamp == coverStringToDate(getStringByMethodName(resultList.get(0), getMethodName)).getTime()) {
                newDtoList.addLast(resultList.remove(0));
            } else {
                Date date = new Date(currentTimestamp);
                newDtoList.addLast(setStringByMethodName(classType.newInstance(), setMethodName, fullFormat.format(date)));
            }
            currentTimestamp += interval;
        }
        return newDtoList;
    }

    private static Date coverStringToDate(String stringDate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.parse(stringDate);
    }
}

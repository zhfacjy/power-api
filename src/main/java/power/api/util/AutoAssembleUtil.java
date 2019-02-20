package power.api.util;

import power.api.controller.responseModel.powerMonitoring.runningReport.LineReportItem;
import power.api.controller.responseModel.powerMonitoring.runningReport.PhaseReportItem;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 自动填充相同成员工具
 */
public class AutoAssembleUtil {

    /**
     * 将withDataObj中与emptyObj同名的属性合并到emptyObj中
     * <p>
     * 原理:
     * 如果withDataObj中有公共方法名为getFuck()，且emptyObj中有名为fuck的属性
     * 则将emptyObj的fuck属性赋值为withDataObj.getFuck()
     * 不区分大小写
     *
     * @param emptyObj
     * @param withDataObj
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void assembleSameNameField(Object emptyObj, Object withDataObj) throws IllegalAccessException, InvocationTargetException {
        Field[] fields = emptyObj.getClass().getDeclaredFields();
        HashMap<String, Object> withDataObjCache = produceFieldCache(withDataObj);
        for (Field field : fields) {
            String keyName = field.getName().toUpperCase();
            if (withDataObjCache.containsKey(keyName)) {
                field.setAccessible(true);
                field.set(emptyObj, withDataObjCache.get(keyName));
            }
        }
    }

    public static <T> void assembleBySpecifiedMethod(Object o, String methodName, T value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = o.getClass().getDeclaredMethod(methodName, value.getClass());
        method.invoke(o, value);
    }

    private static HashMap<String, Object> produceFieldCache(Object o) throws IllegalAccessException, InvocationTargetException {
        HashMap<String, Object> fieldHashMap = new HashMap<>();
        Method[] methods = o.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("get") && returnTypeIsNotVoid(method)) {
                fieldHashMap.put(method.getName().substring(3).toUpperCase(), method.invoke(o));
            }
        }
        return fieldHashMap;
    }

    private static boolean returnTypeIsNotVoid(Method m) {
        return !m.getReturnType().getName().equals("void");
    }
}

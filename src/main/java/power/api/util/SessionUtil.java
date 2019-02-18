
package power.api.util;

import org.springframework.security.core.context.SecurityContextHolder;
import power.api.security.UserDetail;

/**
 * Created by 浩发 on 2019/02/07.
 * 获取当前用户的信息工具类
 */
public class SessionUtil {

    //获取当前用户id
    public static String getCurrUid() {
        try {
            UserDetail userDetail= (UserDetail) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();
            if (userDetail != null) {
                return userDetail.getUserId();
            }
        }catch (Exception e){}
        return null;
    }

    public static void clear() {
        SecurityContextHolder.clearContext();
    }
}

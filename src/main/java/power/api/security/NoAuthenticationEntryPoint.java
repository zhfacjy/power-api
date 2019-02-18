
package power.api.security;

import com.alibaba.fastjson.JSON;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import power.api.common.RestResp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Ztrain on 2017/7/26.
 */

public class NoAuthenticationEntryPoint implements AuthenticationEntryPoint {

    //当访问的资源没有权限调用
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        //返回json形式的错误信息
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        RestResp restResp = new RestResp(RestResp.NO_SESSION, "没有登录或登录已过期!");

        response.getWriter().println(JSON.toJSONString(restResp));
        response.getWriter().flush();
    }
}
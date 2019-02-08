package power.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;
import power.api.dto.RestResp;
import power.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import power.api.repository.UserRepository;
import power.api.security.JwtTokenProvider;

/**
 * Created by 浩发 on 2019/1/28 11:27
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public RestResp login(@RequestBody JSONObject params) {
        String password = DigestUtils.md5Hex(params.getString("password"));
        String mobile = params.getString("mobile");
        User user = userRepository.findByMobileAndPassword(mobile, password);
        if (user == null) {
            return new RestResp(400,"用户名或密码错误！");
        }
        JSONObject uj = new JSONObject();
        uj.put("userId", user.getId());
        String token = jwtTokenProvider.createToken(uj);
        JSONObject result = JSONObject.parseObject(JSON.toJSONString(user));
        result.remove("password");
        result.put("token", token);
        return new RestResp(result);
    }

    @PostMapping("/register")
    public RestResp register(@RequestBody User user) {
        User user1 = userRepository.findByMobile(user.getMobile());
        if (user1 != null) {
            return new RestResp(400, "该手机号已被注册！");
        }
        String password = DigestUtils.md5Hex(user.getPassword());
        user.setPassword(password);
        userRepository.save(user);
        return new RestResp();
    }
}

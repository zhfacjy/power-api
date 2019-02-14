package power.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;
import power.api.controller.paramModel.LoginParam;
import power.api.controller.paramModel.RegisterParam;
import power.api.controller.responseModel.LoginResponse;
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
    public RestResp login(@RequestBody LoginParam loginParam) {
        String password = DigestUtils.md5Hex(loginParam.getPassword());
        String username = loginParam.getUsername();

        User user = userRepository.findByUsernameAndPassword(username, password);

        if (user == null) {
            return new RestResp(RestResp.INVISIBLE, "用户名或密码错误！");
        }

        user.setPassword(null);
        String token = jwtTokenProvider.createToken(user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setUser(user);
        return RestResp.createBySuccess(loginResponse);
    }

    @PostMapping("/register")
    public RestResp register(@RequestBody RegisterParam registerParam) {
        User existUser = userRepository.findByMobile(registerParam.getMobile());
        if (existUser != null) {
            return RestResp.createBy(RestResp.ALREADYEXISTS, "手机号已被注册！");
        }

        existUser = userRepository.findByUsername(registerParam.getUsername());
        if (existUser != null) {
            return RestResp.createBy(RestResp.ALREADYEXISTS, "用户名已被注册！");
        }

        existUser = null;
        String password = DigestUtils.md5Hex(registerParam.getPassword());

        User registerUser = new User();
        registerUser.setMobile(registerParam.getMobile());
        registerUser.setUsername(registerParam.getUsername());
        registerUser.setPassword(password);
        userRepository.save(registerUser);

        registerUser.setPassword(null);

        return RestResp.createBySuccess(registerUser);
    }
}

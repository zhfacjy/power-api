package power.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import power.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import power.api.repository.UserRepository;
import power.api.security.JwtTokenProvider;

import javax.persistence.EntityManager;
import java.util.Optional;

/**
 * Created by 浩发 on 2019/1/28 11:27
 */
@RestController
//@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager em;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/auth/test")
    public JSONObject test() {
        JSONObject result = new JSONObject();
        Optional<User> user = userRepository.findById(1);
//        User user = userRepository.getOne(1);
        JSONObject uj = JSONObject.parseObject(JSON.toJSONString(user));
        String token = jwtTokenProvider.createToken(uj);
        result.put("code", 0);
        result.put("data", token);
        return result;
    }

    @GetMapping("/test")
    public JSONObject test2() {
        JSONObject result = new JSONObject();
        result.put("code", 0);
        result.put("data", null);
        return result;
    }
}

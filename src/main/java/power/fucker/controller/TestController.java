package power.fucker.controller;

import com.alibaba.fastjson.JSONObject;
import power.fucker.repository.DictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Optional;

/**
 * Created by 浩发 on 2019/1/28 11:27
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private DictRepository dictRepository;
    @Autowired
    private EntityManager em;

    @GetMapping
    public JSONObject test() {
        JSONObject result = new JSONObject();
        return result;
    }
}

package power.api.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * Created by 浩发 on 2019/3/5 17:15
 * dict值
 */
public class DictValue {

    // 越限的类型
    public static final JSONArray overLimitType = new JSONArray(){{
        add(new JSONObject(){{
            put("type","01");put("name","高温报警");
        }});
    }};


    // 越限的限定值  key 对应over_limit_event表的type
    public static final HashMap<String, Object> overLimitValue = new HashMap<String, Object>(){{
        put("01", 50);
    }};

}

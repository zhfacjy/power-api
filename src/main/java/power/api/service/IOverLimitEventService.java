package power.api.service;

import com.alibaba.fastjson.JSONObject;
import power.api.controller.paramModel.SearchOverLimitEventParam;

/**
 * Created by 浩发 on 2019/3/5 17:36
 * 越限事件查询
 */
public interface IOverLimitEventService {

    JSONObject search(SearchOverLimitEventParam params, Integer pageNo, Integer pageSize);

}

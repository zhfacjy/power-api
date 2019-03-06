package power.api.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by 浩发 on 2019/2/17 17:28
 * 用电分析
 */
public interface IPowerAnalysisService {

    JSONArray getPowerAnalysis(Integer dateType, String createAt);

    JSONObject yearOnYear(String centralNode);

    JSONArray monthOnMonth(String centralNode,Integer dateType, String createAt);

    JSONArray getCollection(String beginDate,String endDate);
}

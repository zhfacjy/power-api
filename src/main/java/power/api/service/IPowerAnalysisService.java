package power.api.service;

import java.util.HashMap;

/**
 * Created by 浩发 on 2019/2/17 17:28
 * 用电分析
 */
public interface IPowerAnalysisService {

    HashMap<String, Double> getPowerAnalysis(Integer dataType, String createAt);

}

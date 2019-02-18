package power.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;
import power.api.service.IPowerAnalysisService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 浩发 on 2019/2/17 17:31
 */
@Service
public class PowerAnalysisService implements IPowerAnalysisService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public HashMap<String, Double> getPowerAnalysis(Integer dataType, String createAt) {
        HashMap<String, Double> result = new HashMap<>();
        HashMap<String, Double> databases = new HashMap<>();
        Query query;
        SimpleDateFormat sdf;
        SimpleDateFormat sdf2;
        if (dataType == 1) {
            sdf = new SimpleDateFormat("yyyyMMdd");
            Integer today = Integer.valueOf(sdf.format(new Date()));
            Integer paramData = Integer.valueOf(createAt);
            if (today < paramData) {
                result.put("00",0.00);
                return result;
            }
            // 初始化
            Integer times = 23;
            if (today.equals(paramData)) {
                sdf2 = new SimpleDateFormat("HH");
                times = Integer.valueOf(sdf2.format(new Date()));
            }
            // 查询当天的各个时段的初始总电能
            String sql = "select time as \"time\",sum(electric_energy) as \"energy\" from (" +
                    "select min(electric_energy) as electric_energy,meter,time from (" +
                    "select meter,truncate(electric_energy,2) as electric_energy,DATE_FORMAT(create_at,'%H') as time " +
                    "from meter_record where DATE_FORMAT(create_at,'%Y%m%d') = '"+createAt.substring(0,8)+"') as t " +
                    "group by meter,time) as b GROUP BY time";
            query = em.createNativeQuery(sql);
            // 返回dto写法 query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(clazz.class));
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List timeEnergy = query.getResultList();
            if (timeEnergy.size()==0) {
                return result;
            }
            JSONArray array = JSONArray.parseArray(JSON.toJSONString(timeEnergy));
            // 查询最后一个时间段的最大电能值
            String sql2 = "select sum(energy) as energy from (select meter,truncate(max(electric_energy),2) as energy from meter_record " +
                    "where DATE_FORMAT(create_at,'%Y%m%d') = '"+createAt.substring(0,8)+"' GROUP BY meter) as t";
            query = em.createNativeQuery(sql2);
            Double max = (Double)query.getSingleResult();
            for (int i=0;i<array.size();i++) {
                JSONObject object = array.getJSONObject(i);
                databases.put(object.getString("time"),object.getDouble("energy"));
            }
            for (int i=0;i<=times;i++) {
                String time = String.format("%02d",i);
                int j = i + 1;
                if (databases.containsKey(time)&&j<=times) {
                    String time2 = String.format("%02d",j);
                    if (databases.containsKey(time2)) {
                        result.put(time,databases.get(time2) - databases.get(time));
                    } else {
                        result.put(time, 0.0);
                    }
                } else if (databases.containsKey(time)) {
                    result.put(time,max - databases.get(time));
                } else {
                    result.put(time,0.0);
                }
            }

        } else if (dataType == 2) {

        } else {

        }
        return result;
    }
}

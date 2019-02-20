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
import java.util.Calendar;
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
        Query query;
        SimpleDateFormat sdf;
        SimpleDateFormat sdf2;
        // 日报
        if (dataType == 1) {
            sdf = new SimpleDateFormat("yyyyMMdd");
            Integer today = Integer.valueOf(sdf.format(new Date()));
            Integer paramData = Integer.valueOf(createAt);
            // 如果查询日期比今天还要大，直接返回
            if (today < paramData) {
                result.put("00",0.00);
                return result;
            }
            // 初始化
            Integer times = 23;
            // 如果查询日期等于今天，还要判断到达那个时间
            if (today.equals(paramData)) {
                sdf2 = new SimpleDateFormat("HH");
                times = Integer.valueOf(sdf2.format(new Date()));
            }
            // 查询当前时间的所用电能
            String findEnergy = "select sum(energy) as num,hours from (" +
                    "select max(electric_energy) - min(electric_energy) as energy,DATE_FORMAT(create_at,'%H') as hours " +
                    "from meter_record where DATE_FORMAT(create_at,'%Y%m%d') = ?1 GROUP BY meter,DATE_FORMAT(create_at,'%H')) as t " +
                    "group by t.hours";
            query = em.createNativeQuery(findEnergy);
            query.setParameter(1,createAt);
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            JSONArray array = JSONArray.parseArray(JSON.toJSONString(query.getResultList()));
            for (int i=0;i<=times;i++) {
                String time = String.format("%02d",i);
                result.put(time,0.0);
                for (int j=0;j<array.size();j++) {
                    if (array.getJSONObject(j).getString("hours").equals(time)) {
                        result.put(time,array.getJSONObject(j).getDouble("num"));
                        break;
                    }
                }
            }
        // 月报
        } else if (dataType == 2) {
            sdf = new SimpleDateFormat("yyyyMM");
            Integer today = Integer.valueOf(sdf.format(new Date()));
            Integer paramData = Integer.valueOf(createAt);
            if (today < paramData) {
                result.put("00",0.00);
                return result;
            }
            // 初始化
            Calendar calendar = Calendar.getInstance();
            try{
                calendar.setTime(sdf.parse(createAt));
            } catch (Exception e) {
                calendar.setTime(new Date());
            }
            Integer times = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (today.equals(paramData)) {
                sdf2 = new SimpleDateFormat("dd");
                times = Integer.valueOf(sdf2.format(new Date()));
            }
            // 查询当前时间的所用电能
            String findEnergy = "select sum(energy) as num,days from (" +
                    "select max(electric_energy)-min(electric_energy) as energy,DATE_FORMAT(create_at,'%d') as days " +
                    "from meter_record where DATE_FORMAT(create_at,'%Y%m') = ?1 " +
                    "group by meter,DATE_FORMAT(create_at,'%d')) as t group by days";
            query = em.createNativeQuery(findEnergy);
            query.setParameter(1,createAt);
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            JSONArray array = JSONArray.parseArray(JSON.toJSONString(query.getResultList()));
            for (int i=1;i<=times;i++){
                String time = String.format("%02d",i);
                result.put(time,0.0);
                for (int j=0;j<array.size();j++) {
                    if (array.getJSONObject(j).getString("days").equals(time)) {
                        result.put(time,array.getJSONObject(j).getDouble("num"));
                        break;
                    }
                }
            }
        // 年报
        } else {
            sdf = new SimpleDateFormat("yyyy");
            Integer today = Integer.valueOf(sdf.format(new Date()));
            Integer paramData = Integer.valueOf(createAt);
            if (today < paramData) {
                result.put("00",0.00);
                return result;
            }
            // 初始化
            Integer times = 12;
            if (today.equals(paramData)) {
                sdf2 = new SimpleDateFormat("MM");
                times = Integer.valueOf(sdf2.format(new Date()));
            }
            // 查询当年的所用电能
            String findEnergy = "select sum(energy) as num,months from (" +
                    "select max(electric_energy)-min(electric_energy) as energy,meter," +
                    "DATE_FORMAT(create_at,'%m') as months from meter_record " +
                    "where DATE_FORMAT(create_at,'%Y') = ?1 GROUP BY meter,DATE_FORMAT(create_at,'%m')) as t group by t.months";
            query = em.createNativeQuery(findEnergy);
            query.setParameter(1,createAt);
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            JSONArray array = JSONArray.parseArray(JSON.toJSONString(query.getResultList()));
            for (int i=1;i<=times;i++){
                String time = String.format("%02d",i);
                result.put(time,0.0);
                for (int j=0;j<array.size();j++) {
                    if (array.getJSONObject(j).getString("months").equals(time)) {
                        result.put(time,array.getJSONObject(j).getDouble("num"));
                        break;
                    }
                }
            }
        }
        return result;
    }
}

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
import java.util.List;

/**
 * Created by 浩发 on 2019/2/17 17:31
 */
@Service
public class PowerAnalysisService implements IPowerAnalysisService {

    @PersistenceContext
    private EntityManager em;

    @Override
    public JSONObject getPowerAnalysis(Integer dataType, String createAt) {
        JSONObject result = new JSONObject();
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
            String findEnergy = "select max(electric_energy) - min(electric_energy) as energy,meter,DATE_FORMAT(create_at,'%H') as hours " +
                    "from meter_record where DATE_FORMAT(create_at,'%Y%m%d') = ?1 GROUP BY meter,DATE_FORMAT(create_at,'%H')";
            query = em.createNativeQuery(findEnergy);
            query.setParameter(1,createAt);
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            JSONArray array = JSONArray.parseArray(JSON.toJSONString(query.getResultList()));
            // 如果查不出数据，得查有多少个电表
            if (array.size()==0) {
                return getDefault(result,times,0,"时");
            }
            for (int i=0;i<array.size();i++) {
                JSONObject object = array.getJSONObject(i);
                if (!result.containsKey(object.getString("meter"))) {
                    JSONObject map = new JSONObject();
                    for (int j=0;j<=times;j++) {
                        String time = String.format("%02d",j);
                        map.put(time+"时",0);
                    }
                    map.put(object.getString("hours")+"时",object.getDouble("energy"));
                    result.put(object.getString("meter"),map);
                } else {
                    JSONObject map = result.getJSONObject(object.getString("meter"));
                    map.put(object.getString("hours")+"时",object.getDouble("energy"));
                    result.put(object.getString("meter"),map);
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
            String findEnergy = "select max(electric_energy) - min(electric_energy) as energy,meter,DATE_FORMAT(create_at,'%d') as days " +
                    "from meter_record where DATE_FORMAT(create_at,'%Y%m') = ?1 GROUP BY meter,DATE_FORMAT(create_at,'%d')";
            query = em.createNativeQuery(findEnergy);
            query.setParameter(1,createAt);
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            JSONArray array = JSONArray.parseArray(JSON.toJSONString(query.getResultList()));
            // 如果查不出数据，得查有多少个电表
            if (array.size()==0) {
                return getDefault(result,times,1,"日");
            }
            for (int i=0;i<array.size();i++) {
                JSONObject object = array.getJSONObject(i);
                if (!result.containsKey(object.getString("meter"))) {
                    JSONObject map = new JSONObject();
                    for (int j=1;j<=times;j++) {
                        String time = String.format("%02d",j);
                        map.put(time+"日",0);
                    }
                    map.put(object.getString("days")+"日",object.getDouble("energy"));
                    result.put(object.getString("meter"),map);
                } else {
                    JSONObject map = result.getJSONObject(object.getString("meter"));
                    map.put(object.getString("days")+"日",object.getDouble("energy"));
                    result.put(object.getString("meter"),map);
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
            String findEnergy = "select max(electric_energy) - min(electric_energy) as energy,meter,DATE_FORMAT(create_at,'%m') as months " +
                    "from meter_record where DATE_FORMAT(create_at,'%Y') = ?1 GROUP BY meter,DATE_FORMAT(create_at,'%m')";
            query = em.createNativeQuery(findEnergy);
            query.setParameter(1,createAt);
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            JSONArray array = JSONArray.parseArray(JSON.toJSONString(query.getResultList()));
            // 如果查不出数据，得查有多少个电表
            if (array.size()==0) {
                return getDefault(result,times,1,"月");
            }
            for (int i=0;i<array.size();i++) {
                JSONObject object = array.getJSONObject(i);
                if (!result.containsKey(object.getString("meter"))) {
                    JSONObject map = new JSONObject();
                    for (int j=1;j<=times;j++) {
                        String time = String.format("%02d",j);
                        map.put(time+"月",0);
                    }
                    map.put(object.getString("months")+"月",object.getDouble("energy"));
                    result.put(object.getString("meter"),map);
                } else {
                    JSONObject map = result.getJSONObject(object.getString("meter"));
                    map.put(object.getString("months")+"月",object.getDouble("energy"));
                    result.put(object.getString("meter"),map);
                }
            }
        }
        return result;
    }

    @Override
    public JSONObject yearOnYear(String centralNode) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String now = sdf.format(new Date());
        Integer month = Integer.valueOf(now.substring(4,6))+1;
        Integer oldYear = Integer.valueOf(now.substring(0,4)) - 1;
        // 查询某一年的月份的所用电能
        String findEnergy = "select sum(energy) as energy,months from (" +
                "select max(electric_energy) - min(electric_energy) as energy,DATE_FORMAT(create_at,'%m') as months " +
                "from meter_record where DATE_FORMAT(create_at,'%Y') = ?1 and central_node =?2 " +
                "GROUP BY meter,DATE_FORMAT(create_at,'%m')) as a where months < ?3 GROUP BY months";
        Query query = em.createNativeQuery(findEnergy);
        query.setParameter(1,now.substring(0,4));
        query.setParameter(2,centralNode);
        query.setParameter(3,month+"");
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        JSONArray thisYear = JSONArray.parseArray(JSON.toJSONString(query.getResultList()));
        query.setParameter(1,oldYear+"");
        JSONArray lastYear = JSONArray.parseArray(JSON.toJSONString(query.getResultList()));
        JSONObject result = new JSONObject();
        if (thisYear.size()<month-1) {
            for (int i=1;i<month;i++) {
                String months = String.format("%02d",i);
                if (!hasExists(months,thisYear)) {
                    JSONObject object = new JSONObject();
                    object.put("months",months);
                    object.put("energy",0);
                    thisYear.add(object);
                }
            }
        }
        if (lastYear.size()<month-1) {
            for (int i=1;i<month;i++) {
                String months = String.format("%02d",i);
                if (!hasExists(months,lastYear)) {
                    JSONObject object = new JSONObject();
                    object.put("months",months);
                    object.put("energy",0);
                    lastYear.add(object);
                }
            }
        }
        result.put("thisYear",thisYear);
        result.put("lastYear",lastYear);
        return result;
    }

    private boolean hasExists(String moth,JSONArray array) {
        for (int i = 0;i<array.size();i++) {
            if (array.getJSONObject(i).getString("months").equals(moth)) {
                return true;
            }
        }
        return false;
    }

    private JSONObject getDefault(JSONObject result, Integer times, int i, String type) {
        String findMeter = "select distinct meter from meter_record";
        Query query = em.createNativeQuery(findMeter);
        List<String> meters = query.getResultList();
        JSONObject energy = new JSONObject();
        for (;i<=times;i++) {
            String time = String.format("%02d",i);
            energy.put(time+type,0);
        }
        for (String meter : meters) {
            result.put(meter,energy);
        }
        return result;
    }

}

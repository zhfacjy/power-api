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
import java.util.Comparator;
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
        String findEnergy = "select truncate(sum(energy),0) as energy,months from (" +
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
        // 根据月份正序
        thisYear.sort(Comparator.comparing(obj -> ((JSONObject) obj).getString("months")));
        lastYear.sort(Comparator.comparing(obj -> ((JSONObject) obj).getString("months")));
        // 同比比率
        JSONArray percent = new JSONArray();
        for (int i=0;i<thisYear.size();i++) {
            JSONObject object = new JSONObject();
            object.put("percent1",0);
            object.put("percent2",0);
            Double nowEnergy = thisYear.getJSONObject(i).getDouble("energy");
            Double oldEnergy = lastYear.getJSONObject(i).getDouble("energy");
            if (oldEnergy > 0) {
                double c1 = (nowEnergy - oldEnergy) / oldEnergy; // 同比率
                String cent1 = String.format("%.2f", c1);
                object.put("percent1",cent1);
                if (i > 0) {
                    object.put("percent2",getPerCent2(thisYear,lastYear,i));
                } else {
                    object.put("percent2",cent1);
                }
            }
            percent.add(object);
        }
        result.put("thisYear",thisYear);
        result.put("lastYear",lastYear);
        result.put("percent",percent);
        return result;
    }

    @Override
    public JSONObject monthOnMonth(String centralNode,Integer dataType, String createAt) {
        JSONObject result = new JSONObject();
        Query query;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance(); // 判断大于明天或下周或下月
        calendar.setTime(new Date());
        Integer paramData = Integer.valueOf(createAt);

        // 按日环比
        if (dataType == 1) {
            calendar.add(Calendar.DATE, 1);
            Integer tomorrow = Integer.valueOf(sdf.format(calendar.getTime()));
            // 如果比明天还大的日期，2天都为0
            if (tomorrow < paramData) {
                getDefault2(centralNode,result);
                return result;
            }
            // 获取传入参数的前一天
            Calendar cd = Calendar.getInstance();
            try {
                cd.setTime(sdf.parse(createAt));
            } catch (Exception e) {
                cd.setTime(new Date());
            }
            cd.add(Calendar.DATE, -1);
            String yesterday = sdf.format(cd.getTime());
            String findEnergy = "select truncate(max(electric_energy) - min(electric_energy),2) as energy,meter from meter_record " +
                    "where DATE_FORMAT(create_at,'%Y%m%d') = ?1 and central_node = ?2 GROUP BY meter";
            query = em.createNativeQuery(findEnergy);
            query.setParameter(2,centralNode);
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            query.setParameter(1,yesterday);
            JSONArray yesterDay = JSONArray.parseArray(JSON.toJSONString(query.getResultList()));
            query.setParameter(1,createAt);
            JSONArray todayDay = JSONArray.parseArray(JSON.toJSONString(query.getResultList()));
            // 如果某一天的值丢失了，也得显示有值的一天
            result = lastAndCurrent(yesterDay,todayDay,result,centralNode);
        // 按周环比
        } else if (dataType == 2) {
            // 获取下周周日
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            int nextMondayOffset = dayOfWeek == 1 ? 7 : 15 - dayOfWeek;
            calendar.add(Calendar.DAY_OF_MONTH, nextMondayOffset);
            Integer nextWeek = Integer.valueOf(sdf.format(calendar.getTime()));
            // 如果比下周周日还大的日期，2周都为0
            if (nextWeek < paramData) {
                getDefault2(centralNode,result);
                return result;
            }

            // 获取传入参数的前一周
            Calendar cd = Calendar.getInstance();
            try {
                cd.setTime(sdf.parse(createAt));
            } catch (Exception e) {
                cd.setTime(new Date());
            }
            int dayOfWeek2 = cd.get(Calendar.DAY_OF_WEEK) - 1;
            int offset1 = 1 - dayOfWeek2;
            cd.add(Calendar.DATE, offset1 - 7);
            String lastMonday = sdf.format(cd.getTime());
            cd.add(Calendar.DATE,7);
            String nowMonday = sdf.format(cd.getTime());
            cd.add(Calendar.DATE,-1);
            String lastSunday = sdf.format(cd.getTime());
            cd.add(Calendar.DATE, 7);
            String nowSunday = sdf.format(cd.getTime());

            String findEnergy = "select truncate(max(electric_energy) - min(electric_energy),2) as energy,meter from meter_record " +
                    "where DATE_FORMAT(create_at,'%Y%m%d') BETWEEN ?1 and ?2 and central_node = ?3 GROUP BY meter";
            query = em.createNativeQuery(findEnergy);
            query.setParameter(1,lastMonday);
            query.setParameter(2,lastSunday);
            query.setParameter(3,centralNode);
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            JSONArray lastWeek = JSONArray.parseArray(JSON.toJSONString(query.getResultList()));
            query.setParameter(1,nowMonday);
            query.setParameter(2,nowSunday);
            JSONArray currentWeek = JSONArray.parseArray(JSON.toJSONString(query.getResultList()));
            // 如果某一周的值丢失了，也得显示有值的一周
            result = lastAndCurrent(lastWeek,currentWeek,result,centralNode);
        // 按月环比
        } else {
            calendar.add(Calendar.MONTH, 2);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
            Integer nextMonth = Integer.valueOf(sdf2.format(calendar.getTime()));
            // 如果下下月等于传入日期，2个月都为0
            if (nextMonth == Integer.valueOf(createAt.substring(0,6))) {
                getDefault2(centralNode,result);
                return result;
            }
            // 获取出入日期的上一个月
            Calendar cd = Calendar.getInstance();
            try {
                cd.setTime(sdf.parse(createAt));
            } catch (Exception e) {
                cd.setTime(new Date());
            }
            cd.add(Calendar.MONTH,-1);
            String lastMonthStr = sdf2.format(cd.getTime());
            // 获取传入日期的月份
            String findEnergy = "select truncate(max(electric_energy) - min(electric_energy),2) as energy,meter from meter_record " +
                    "where DATE_FORMAT(create_at,'%Y%m') = ?1 and central_node = ?2 GROUP BY meter";
            query = em.createNativeQuery(findEnergy);
            query.setParameter(1,createAt.substring(0,6));
            query.setParameter(2,centralNode);
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            JSONArray currentMonth = JSONArray.parseArray(JSON.toJSONString(query.getResultList()));
            query.setParameter(1,lastMonthStr);
            JSONArray lastMonth = JSONArray.parseArray(JSON.toJSONString(query.getResultList()));
            // 如果某一个月的值丢失了，也得显示有值的一个月
            result = lastAndCurrent(lastMonth,currentMonth,result,centralNode);
        }
        return result;
    }

    // 判断当前年份内的月份是否存在
    private boolean hasExists(String moth,JSONArray array) {
        for (int i = 0;i<array.size();i++) {
            if (array.getJSONObject(i).getString("months").equals(moth)) {
                return true;
            }
        }
        return false;
    }

    // 计算累计同比率
    private String getPerCent2(JSONArray thisYear,JSONArray lastYear, int i) {
        Double sumNowEnergy = 0.0;
        Double sumOldEnergy = 0.0;
        for (int j=0;j<=i;j++) {
            sumNowEnergy += thisYear.getJSONObject(j).getDouble("energy");
            sumOldEnergy += lastYear.getJSONObject(j).getDouble("energy");
        }
        double result = (sumNowEnergy - sumOldEnergy) / sumOldEnergy;
        return String.format("%.2f", result);
    }

    // 用能分析日期大于今日时间的默认值
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

    // 环比分析日期大于明天时间的默认值
    private JSONObject getDefault2(String centralNode,JSONObject result) {
        String findMeter = "select distinct meter from meter_record where central_node = ?1";
        Query query = em.createNativeQuery(findMeter);
        query.setParameter(1,centralNode);
        List<String> meters = query.getResultList();
        for (String meter : meters) {
            JSONObject object = new JSONObject();
            object.put("today",0);
            object.put("yesterday",0);
            object.put("addValue",0);
            object.put("percent",0);
            result.put(meter,object);
        }
        return result;
    }

    // 环比比分析的缺省判断
    private JSONObject lastAndCurrent(JSONArray lastDays,JSONArray currentDays,JSONObject result,String centralNode) {
        if (lastDays.size()>0&&currentDays.size()>0) {
            for (int i=0;i<currentDays.size();i++) {
                JSONObject object = currentDays.getJSONObject(i);
                // 调用了hasExists方法
                Double double1 = hasExists(object,lastDays);
                Double addValue = object.getDouble("energy")-double1;

                JSONObject param = new JSONObject();
                param.put("current",object.getDouble("energy"));
                param.put("lastTime",double1);
                param.put("addValue",String.format("%.2f", addValue));
                param.put("percent","1");
                if (double1 != 0.0) {
                    double percent = addValue / double1;
                    param.put("percent",String.format("%.2f", percent));
                }
                result.put(object.getString("meter"),param);
            }
        } else if (lastDays.size()==0&&currentDays.size()==0) {
            // 调用了getDefault2方法
            getDefault2(centralNode,result);
            return result;
        } else if (lastDays.size()==0&&currentDays.size()!=0) {
            // 调用了setValue方法
            setValue(result,currentDays,true);
        } else {
            // 调用了setValue方法
            setValue(result,lastDays,false);
        }
        return result;
    }

    // 环比分析某一天缺省的赋值
    private void setValue(JSONObject result,JSONArray days,Boolean isCurrent) {
        for (int i=0;i<days.size();i++) {
            JSONObject object = days.getJSONObject(i);
            Double double1 = 0.0;
            JSONObject param = new JSONObject();
            if (isCurrent) {
                Double addValue = object.getDouble("energy")-double1;
                param.put("addValue",String.format("%.2f", addValue));
                param.put("percent","1");
                param.put("current",object.getDouble("energy"));
                param.put("lastTime",double1);
            } else {
                param.put("addValue",String.format("%.2f", -object.getDouble("energy")));
                param.put("percent","-1");
                param.put("current",double1);
                param.put("lastTime",object.getDouble("energy"));
            }
            result.put(object.getString("meter"),param);
        }
    }

    // 查询是否拥有相同电表数据
    private Double hasExists(JSONObject param,JSONArray days) {
        for (int i=0;i<days.size();i++) {
            if (param.getString("meter").equals(days.getJSONObject(i).getString("meter"))) {
                return days.getJSONObject(i).getDouble("energy");
            }
        }
        return 0.0;
    }

}

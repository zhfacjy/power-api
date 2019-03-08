package power.api.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import power.api.model.OverLimitEvent;
import power.api.repository.OverLimitEventRepository;
import power.api.util.DictValue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 浩发 on 2019/2/22 17:22
 * 定时统计越限事件
 */
@Component
public class ScanOverLimitEventSchedule {

    // 没有记录到数据库，只能记录在类里面的最后运行时间，如果项目重启了，该时间也会变动
    private Date lastRunTime = new Date();

    @Autowired
    private OverLimitEventRepository overLimitEventRepository;
    @PersistenceContext
    private EntityManager em;

    // 温度越限 10分钟执行一次
    @Scheduled(cron = "0 0/10 * * * ?")
    @Transactional
    public void temperatureOver() {
        // 先查询上一次是否存在越限未结束的记录
        String findHasExist = "select id,meter,begin_date as beginDate from over_limit_event where end_date is null GROUP BY meter";
        Query query2 = em.createNativeQuery(findHasExist);
        query2.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        JSONArray hasExist = JSONArray.parseArray(JSON.toJSONString(query2.getResultList()));
        // 如果存在，先查出他们的结束时间
        if (hasExist.size()>0) {
            String sql = "select create_at from meter_record where id =" +
                    "(select min(id) from meter_record where temperature < ?1 and create_at > ?2 and meter = ?3)";
            query2 = em.createNativeQuery(sql);
            query2.setParameter(1,DictValue.overLimitValue.get("01"));
            for (int i=0;i<hasExist.size();i++) {
                JSONObject object = hasExist.getJSONObject(i);
                query2.setParameter(2,object.getDate("beginDate"));
                query2.setParameter(3,object.getString("meter"));
                List<Date> rlt = query2.getResultList();
                // 查出单个电表的越限结束时间
                if (rlt.size()>0) {
                    overLimitEventRepository.updateEndDateById(rlt.get(0),object.getInteger("id"));
                }
            }
        }

        // 查询各个电表比lastRunTime还大的时间的最早越限id和时间
        String findMinOver = "select a.meter,min(a.id) as id,b.create_at as createAt,b.temperature from meter_record a " +
                "left join meter_record b on a.id = b.id " +
                "where a.temperature >= ?1 and a.create_at > ?2 GROUP BY a.meter";
        Query query = em.createNativeQuery(findMinOver);
        query.setParameter(1, DictValue.overLimitValue.get("01"));
        query.setParameter(2,lastRunTime);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(query.getResultList()));
        // 出现越限事件
        if (jsonArray.size()>0) {
            // 查询越限结束时间
            String findEnd = "select create_at from meter_record where id =" +
                    "(select min(id) from meter_record where id > ?1 and temperature < ?2 and meter = ?3)";
            query = em.createNativeQuery(findEnd);
            query.setParameter(2,DictValue.overLimitValue.get("01"));
            for (int i=0;i<jsonArray.size();i++) {
                // 查出是否存在已结束的时间
                JSONObject object = jsonArray.getJSONObject(i);
                query.setParameter(1,object.getInteger("id"));
                query.setParameter(3,object.getString("meter"));
                List<Date> rlt = query.getResultList();

                OverLimitEvent overLimitEvent = new OverLimitEvent();
                overLimitEvent.setBeginDate(object.getDate("createAt"));
                overLimitEvent.setDefaultValue(String.valueOf(DictValue.overLimitValue.get("01")));
                overLimitEvent.setMeter(object.getString("meter"));
                overLimitEvent.setType("01");
                overLimitEvent.setWarningValue(object.getInteger("temperature")+"");
                // 如果存在已结束的话，可能后续还有越限事件，形成递归
                if (rlt.size()>0) {
                    overLimitEvent.setEndDate(rlt.get(0));
                    // 调用递归方法
                    findMoreTemperatureOver(rlt.get(0),overLimitEvent.getMeter());
                }
                overLimitEventRepository.save(overLimitEvent);
            }
        }
        lastRunTime = new Date();
    }

    // 查询更多的温度越限事件
    private void findMoreTemperatureOver(Date createAt,String meter) {
        String findMore = "select create_at as createAt,temperature from meter_record where id =(" +
                "select min(id) from meter_record where temperature >= ?1 and create_at > ?2 and meter = ?3)";
        Query query = em.createNativeQuery(findMore);
        query.setParameter(1,DictValue.overLimitValue.get("01"));
        query.setParameter(2,createAt);
        query.setParameter(3,meter);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        JSONArray rlt = JSONArray.parseArray(JSON.toJSONString(query.getResultList()));
        if (rlt.size() > 0) {
            // 查询是否符合递归的条件（是否还存在后续，比如存在结束时间，然后又查询是否存在越限）
            String findMoreEnd = "select create_at as createAt from meter_record where id =(" +
                    "select min(id) from meter_record where temperature < ?1 and create_at > ?2 and meter = ?3)";

            OverLimitEvent overLimitEvent = new OverLimitEvent();
            overLimitEvent.setBeginDate(rlt.getJSONObject(0).getDate("createAt"));
            overLimitEvent.setDefaultValue(String.valueOf(DictValue.overLimitValue.get("01")));
            overLimitEvent.setMeter(meter);
            overLimitEvent.setType("01");
            overLimitEvent.setWarningValue(rlt.getJSONObject(0).getInteger("temperature")+"");

            query = em.createNativeQuery(findMoreEnd);
            query.setParameter(1,DictValue.overLimitValue.get("01"));
            query.setParameter(2,rlt.getJSONObject(0).getDate("createAt"));
            query.setParameter(3,meter);

            List<Date> hasEnd = query.getResultList();
            if (hasEnd.size()>0) {
                overLimitEvent.setEndDate(hasEnd.get(0));
                this.findMoreTemperatureOver(hasEnd.get(0),meter);
            }
            overLimitEventRepository.save(overLimitEvent);
        }
    }

}

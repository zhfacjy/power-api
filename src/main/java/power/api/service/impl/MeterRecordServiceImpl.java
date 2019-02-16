package power.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import power.api.controller.paramModel.GetElectricDataParam;
import power.api.controller.responseModel.powerMonitoring.ActivePowerResponse;
import power.api.common.RestResp;
import power.api.model.MeterRecord;
import power.api.repository.MeterRecordRepository;
import power.api.service.IMeterRecordService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 浩发 on 2019/2/4 13:46
 * 实现和电表数据有关计算的接口
 * 如功率、电流等等等，需要使用电表数据的方法都在此类中定义
 */
@Service
public class MeterRecordServiceImpl implements IMeterRecordService {

    @Autowired
    private MeterRecordRepository meterRecordRepository;

    @Override
    public RestResp countActivePowerData(long createAt, GetElectricDataParam getElectricDataParam) {
        long remain = createAt + getRemainMillisSecondsOneDay(createAt);
        return this.countActivePowerDataRange(createAt, remain, getElectricDataParam);
    }

    /**
     * 计算时间区间内的三相有功功率
     * @param start
     * @param end
     * @param getElectricDataParam
     * @return
     */
    @Override
    public RestResp countActivePowerDataRange(long start, long end, GetElectricDataParam getElectricDataParam) {
        Timestamp startAt = new Timestamp(start);
        Timestamp endAt = new Timestamp(end);

        List<MeterRecord> meterRecordList = meterRecordRepository.findByCreateAtGreaterThanEqualAndCreateAtLessThanEqual(startAt, endAt);
        List<ActivePowerResponse> activePowerResponseList = new ArrayList<>(meterRecordList.size());

        /**
         * factor = power / (ia * va + ib * vb + ic * vc)
         * pa = ia * va * factor
         * pb = ib * vb * factor
         * pc = ic * vc * factor
         */

        for (MeterRecord m : meterRecordList) {
            // 计算三相功率
            float powerA = m.getIa() * m.getVa();
            float powerB = m.getIb() * m.getVb();
            float powerC = m.getIc() * m.getVc();
            // 计算功率因数
            float factor = (float) (m.getPower() / (powerA + powerB + powerC));

            // 使用自定义的响应对象，用于前端显示
            ActivePowerResponse activePowerResponse = new ActivePowerResponse();

            activePowerResponse.setCreateAt(m.getCreateAt());

            // 如果需要显示A相功率，则将数据放入响应对象中
            if (getElectricDataParam.getPhaseA()) {
                float pa = powerA * factor;
                activePowerResponse.setPa(pa);
            }

            if (getElectricDataParam.getPhaseB()) {
                float pb = powerB * factor;
                activePowerResponse.setPb(pb);
            }

            if (getElectricDataParam.getPhaseC() != null && getElectricDataParam.getPhaseC()) {
                float pc = powerC * factor;
                activePowerResponse.setPc(pc);
            }

            if (getElectricDataParam.getTotal()) {
                activePowerResponse.setP(m.getPower());
            }
            activePowerResponseList.add(activePowerResponse);
        }

        return RestResp.createBySuccess(activePowerResponseList);
    }

    /**
     * 给定一个时间戳，计算离当天24:00的毫秒数
     * 如计算22:11和24:00相差的毫秒数
     * @param currentTimestamp
     * @return
     */
    private long getRemainMillisSecondsOneDay(long currentTimestamp) {
        LocalDateTime midnight = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimestamp),
                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimestamp),
                ZoneId.systemDefault());
        return ChronoUnit.MILLIS.between(currentDateTime, midnight);
    }
}

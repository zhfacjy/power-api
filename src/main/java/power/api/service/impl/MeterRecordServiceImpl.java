package power.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import power.api.controller.paramModel.GetElectricDataParam;
import power.api.dto.ActivePower;
import power.api.dto.RestResp;
import power.api.model.MeterRecord;
import power.api.repository.MeterRecordRepository;
import power.api.service.IMeterRecordService;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 浩发 on 2019/2/4 13:46
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
        List<ActivePower> activePowerList = new ArrayList<>(meterRecordList.size());

        /**
         * factor = power / (ia * va + ib * vb + ic * vc)
         * pa = ia * va * factor
         * pb = ib * vb * factor
         * pc = ic * vc * factor
         */

        for (MeterRecord m : meterRecordList) {
            float powerA = m.getIa() * m.getVa();
            float powerB = m.getIb() * m.getVb();
            float powerC = m.getIc() * m.getVc();
            float factor = (float) (m.getPower() / (powerA + powerB + powerC));
            ActivePower activePower = new ActivePower();
            activePower.setCreateAt(m.getCreateAt());
            if (getElectricDataParam.getPhaseA()) {
                float pa = powerA * factor;
                activePower.setPa(pa);
            }
            if (getElectricDataParam.getPhaseB()) {
                float pb = powerB * factor;
                activePower.setPb(pb);
            }
            if (getElectricDataParam.getPhaseC() != null && getElectricDataParam.getPhaseC()) {
                float pc = powerC * factor;
                activePower.setPc(pc);
            }
            if (getElectricDataParam.getTotal()) {
                activePower.setP(m.getPower());
            }
            activePowerList.add(activePower);
        }

        return RestResp.createBySuccess(activePowerList);
    }

    private long getRemainMillisSecondsOneDay(long currentTimestamp) {
        LocalDateTime midnight = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimestamp),
                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimestamp),
                ZoneId.systemDefault());
        return ChronoUnit.MILLIS.between(currentDateTime, midnight);
    }
}

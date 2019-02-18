package power.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import power.api.common.RestResp;
import power.api.controller.paramModel.GetElectricDataParam;
import power.api.controller.responseModel.powerMonitoring.*;
import power.api.controller.responseModel.powerMonitoring.runningReport.PhaseReportItem;
import power.api.controller.responseModel.powerMonitoring.runningReport.ReportResponse;
import power.api.dto.MaxAvgMinDto;
import power.api.dto.PhaseVoltageReportDto;
import power.api.model.MeterRecord;
import power.api.repository.MeterRecordRepository;
import power.api.service.IMeterRecordService;
import power.api.service.impl.DataWrapperClassHolder.DegreeOfThreePhaseUnbalanceHolder;
import power.api.service.impl.DataWrapperClassHolder.LineVoltageHolder;
import power.api.service.impl.DataWrapperClassHolder.PhaseHolder;
import power.api.util.AutoAssembleUtil;
import power.api.util.PaddingTimeUtil;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 浩发 on 2019/2/4 13:46
 */
@Service
public class MeterRecordService implements IMeterRecordService {

    @Autowired
    private MeterRecordRepository meterRecordRepository;


    @Override
    public RestResp countActivePowerData(long createAt, GetElectricDataParam getElectricDataParam) {
        long remain = createAt + getRemainMillisSecondsOneDay(createAt);
        return this.countActivePowerDataRange(createAt, remain, getElectricDataParam);
    }

    @Override
    public RestResp countActivePowerDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam) {
        List<MeterRecord> meterRecordList = getMeterRecordList(startAt, endAt);
        List<ActivePowerResponse> activePowerResponsesList = new ArrayList<>(meterRecordList.size());
        for (MeterRecord m : meterRecordList) {
            ActivePowerResponse activePowerResponse = new ActivePowerResponse();
            activePowerResponse.setCreateAt(m.getCreateAt());
            activePowerResponse.setActivePowerTotal(m.getActivePower());
            activePowerResponsesList.add(activePowerResponse);

        }


        //注释不要删以后要用
/*       PhaseHolder activePowerHolder = new PhaseHolder();

        for (MeterRecord m : meterRecordList) {
            activePowerHolder.setPhaseA( m.getIa() * m.getVa() * m.getPfa());
            activePowerHolder.setPhaseB(m.getIb() * m.getVb() * m.getPfa()) ;
            activePowerHolder.setPhaseC(m.getIc() * m.getVc() * m.getPfa());
            ActivePowerResponse activePowerResponse = new ActivePowerResponse();
            activePowerResponse.setCreateAt(m.getCreateAt());
            activePowerResponse.setActivePowerA(activePowerHolder.getPhaseA());

            activePowerResponse.setActivePowerB(activePowerHolder.getPhaseB());
            activePowerResponse.setActivePowerC(activePowerHolder.getPhaseC());

            activePowerResponse.setActivePowerTotal(activePowerHolder.getPhaseA() + activePowerHolder.getPhaseB() + activePowerHolder.getPhaseC());
            activePowerResponsesList.add(activePowerResponse);
        }*/

        return RestResp.createBySuccess(activePowerResponsesList);
    }

    @Override
    public RestResp countApparentPowerData(long createAt, GetElectricDataParam getElectricDataParam) {
        long remain = createAt + getRemainMillisSecondsOneDay(createAt);
        return this.countApparentPowerDataRange(createAt, remain, getElectricDataParam);
    }

    /**
     * 计算时间区间内的三相有功功率
     *
     * @param startAt
     * @param endAt
     * @param getElectricDataParam
     * @return
     */
    @Override
    public RestResp countApparentPowerDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam) {
        List<MeterRecord> meterRecordList = getMeterRecordList(startAt, endAt);
        List<ApparentPowerResponse> apparentPowerResponsesList = new ArrayList<>(meterRecordList.size());

        /**
         * factor = power / (ia * va + ib * vb + ic * vc)
         * pa = ia * va * factor
         * pb = ib * vb * factor
         * pc = ic * vc * factor
         */
        PhaseHolder apparentPowerHolder = new PhaseHolder();
        for (MeterRecord m : meterRecordList) {
            apparentPowerHolder.setPhaseA(m.getIa() * m.getVa());
            apparentPowerHolder.setPhaseB(m.getIb() * m.getVb());
            apparentPowerHolder.setPhaseC(m.getIc() * m.getVc());

            ApparentPowerResponse apparentPowerResponse = new ApparentPowerResponse();

            apparentPowerResponse.setCreateAt(m.getCreateAt());
            apparentPowerResponse.setApparentPowerA(apparentPowerHolder.getPhaseA());
            apparentPowerResponse.setApparentPowerB(apparentPowerHolder.getPhaseB());
            apparentPowerResponse.setApparentPowerC(apparentPowerHolder.getPhaseC());
            apparentPowerResponse.setApparentPowerTotal(apparentPowerHolder.getPhaseA() + apparentPowerHolder.getPhaseB() + apparentPowerHolder.getPhaseC());

            apparentPowerResponsesList.add(apparentPowerResponse);
        }

        return RestResp.createBySuccess(apparentPowerResponsesList);
    }


    @Override
    public RestResp countElectricEnergyData(long createAt, GetElectricDataParam getElectricDataParam) {
        long remain = createAt + getRemainMillisSecondsOneDay(createAt);
        return this.countElectricEnergyDataRange(createAt, remain, getElectricDataParam);
    }

    @Override
    public RestResp countElectricEnergyDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam) {
        List<MeterRecord> meterRecordList = getMeterRecordList(startAt, endAt);
        List<ElectricEnergyResponse> electricEnergyResponsesList = new ArrayList<>(meterRecordList.size());

        for (MeterRecord m : meterRecordList) {

            ElectricEnergyResponse electricEnergyResponse = new ElectricEnergyResponse();

            electricEnergyResponse.setCreateAt(m.getCreateAt());
            electricEnergyResponse.setElectricEnergy(m.getElectricEnergy());

            electricEnergyResponsesList.add(electricEnergyResponse);
        }

        return RestResp.createBySuccess(electricEnergyResponsesList);
    }

    @Override
    public RestResp countTemperatureData(long createAt, GetElectricDataParam getElectricDataParam) {
        long remain = createAt + getRemainMillisSecondsOneDay(createAt);
        return this.countTemperatureDataRange(createAt, remain, getElectricDataParam);
    }

    @Override
    public RestResp countTemperatureDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam) {
        List<MeterRecord> meterRecordList = getMeterRecordList(startAt, endAt);
        List<TemperatureResponse> temperatureResponsesList = new ArrayList<>(meterRecordList.size());

        for (MeterRecord m : meterRecordList) {

            TemperatureResponse temperatureResponse = new TemperatureResponse();

            temperatureResponse.setCreateAt(m.getCreateAt());
            temperatureResponse.setTemperature(m.getTemperature());

            temperatureResponsesList.add(temperatureResponse);
        }

        return RestResp.createBySuccess(temperatureResponsesList);
    }

    @Override
    public RestResp countPhaseCurrentData(long createAt, GetElectricDataParam getElectricDataParam) {
        long remain = createAt + getRemainMillisSecondsOneDay(createAt);
        return this.countPhaseCurrentDataRange(createAt, remain, getElectricDataParam);
    }

    @Override
    public RestResp countPhaseCurrentDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam) {
        List<MeterRecord> meterRecordList = getMeterRecordList(startAt, endAt);
        List<PhaseCurrentResponse> phaseCurrentResponseList = new ArrayList<>(meterRecordList.size());

        for (MeterRecord m : meterRecordList) {

            PhaseCurrentResponse phaseCurrentResponse = new PhaseCurrentResponse();

            phaseCurrentResponse.setCreateAt(m.getCreateAt());
            phaseCurrentResponse.setPhaseCurrentA(m.getIa());
            phaseCurrentResponse.setPhaseCurrentB(m.getIb());
            phaseCurrentResponse.setPhaseCurrentC(m.getIc());
            phaseCurrentResponseList.add(phaseCurrentResponse);
        }

        return RestResp.createBySuccess(phaseCurrentResponseList);
    }

    @Override
    public RestResp countPhaseVoltageData(long createAt, GetElectricDataParam getElectricDataParam) {
        long remain = createAt + getRemainMillisSecondsOneDay(createAt);
        return this.countPhaseVoltageDataRange(createAt, remain, getElectricDataParam);
    }

    @Override
    public RestResp countPhaseVoltageDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam) {
        List<MeterRecord> meterRecordList = getMeterRecordList(startAt, endAt);
        List<PhaseVoltageResponse> phaseVoltageResponseList = new ArrayList<>(meterRecordList.size());

        for (MeterRecord m : meterRecordList) {

            PhaseVoltageResponse phaseVoltageResponse = new PhaseVoltageResponse();

            phaseVoltageResponse.setCreateAt(m.getCreateAt());
            phaseVoltageResponse.setPhaseVoltageA(m.getVa());
            phaseVoltageResponse.setPhaseVoltageB(m.getVb());
            phaseVoltageResponse.setPhaseVoltageC(m.getVc());
            phaseVoltageResponseList.add(phaseVoltageResponse);
        }

        return RestResp.createBySuccess(phaseVoltageResponseList);
    }

    @Override
    public RestResp countLineVoltageData(long createAt, GetElectricDataParam getElectricDataParam) {
        long remain = createAt + getRemainMillisSecondsOneDay(createAt);
        return this.countLineVoltageDataRange(createAt, remain, getElectricDataParam);
    }

    @Override
    public RestResp countLineVoltageDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam) {
        List<MeterRecord> meterRecordList = getMeterRecordList(startAt, endAt);
        List<LineVoltageResponse> lineVoltageResponseList = new ArrayList<>(meterRecordList.size());

        LineVoltageHolder lineVoltageHolder = new LineVoltageHolder();

        for (MeterRecord m : meterRecordList) {
            lineVoltageHolder.setUab((float) (Math.sqrt(Math.pow(m.getVa(), 2)
                    + Math.pow(m.getVb(), 2)
                    - 2 * m.getVa() * m.getVb() * Math.cos(Math.PI * 2 / 3))));

            lineVoltageHolder.setUbc((float) (Math.sqrt(Math.pow(m.getVb(), 2)
                    + Math.pow(m.getVc(), 2)
                    - 2 * m.getVb() * m.getVc() * Math.cos(Math.PI * 2 / 3))));

            lineVoltageHolder.setUca((float) (Math.sqrt(Math.pow(m.getVc(), 2)
                    + Math.pow(m.getVa(), 2)
                    - 2 * m.getVc() * m.getVa() * Math.cos(Math.PI * 2 / 3))));


            LineVoltageResponse lineVoltageResponse = new LineVoltageResponse();

            lineVoltageResponse.setCreateAt(m.getCreateAt());
            lineVoltageResponse.setUab(lineVoltageHolder.getUab());
            lineVoltageResponse.setUbc(lineVoltageHolder.getUbc());
            lineVoltageResponse.setUca(lineVoltageHolder.getUca());
            lineVoltageResponseList.add(lineVoltageResponse);
        }

        return RestResp.createBySuccess(lineVoltageResponseList);
    }

    @Override
    public RestResp countPowerFactorData(long createAt, GetElectricDataParam getElectricDataParam) {
        long remain = createAt + getRemainMillisSecondsOneDay(createAt);
        return this.countPowerFactorDataRange(createAt, remain, getElectricDataParam);
    }

    @Override
    public RestResp countPowerFactorDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam) {
        List<MeterRecord> meterRecordList = getMeterRecordList(startAt, endAt);
        List<PowerFactorResponse> powerFactorResponseList = new ArrayList<>(meterRecordList.size());

        for (MeterRecord m : meterRecordList) {

            PowerFactorResponse powerFactorResponse = new PowerFactorResponse();

            powerFactorResponse.setCreateAt(m.getCreateAt());
            powerFactorResponse.setPowerFactorA(m.getPfa());
            powerFactorResponse.setPowerFactorB(m.getPfb());
            powerFactorResponse.setPowerFactorC(m.getPfc());
            powerFactorResponse.setPowerFactorTotal(m.getPfa() + m.getPfb() + m.getPfc());
            powerFactorResponseList.add(powerFactorResponse);
        }

        return RestResp.createBySuccess(powerFactorResponseList);
    }

    @Override
    public RestResp countReactivePowerData(long createAt, GetElectricDataParam getElectricDataParam) {
        long remain = createAt + getRemainMillisSecondsOneDay(createAt);
        return this.countReactivePowerDataRange(createAt, remain, getElectricDataParam);
    }

    @Override
    public RestResp countReactivePowerDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam) {
        List<MeterRecord> meterRecordList = getMeterRecordList(startAt, endAt);
        List<ReactivePowerResponse> reactivePowerResponseList = new ArrayList<>(meterRecordList.size());

        float apparentPowerTotal;

        for (MeterRecord m : meterRecordList) {
            ReactivePowerResponse reactivePowerResponse = new ReactivePowerResponse();

            apparentPowerTotal = (m.getVa() + m.getVb() + m.getVc()) * (m.getIa() + m.getIb() + m.getIc());

            reactivePowerResponse.setReactivePowerTotal(apparentPowerTotal * (-Math.cos(Math.PI / 2 + Math.toDegrees(Math.acos(m.getActivePower() / apparentPowerTotal)))));


        }


        //以下注释不要删，以后要用的
/*        PhaseHolder reactivePowerResponseHolder = new PhaseHolder();

        for (MeterRecord m : meterRecordList) {
            reactivePowerResponseHolder.setPhaseA(m.getVa() * m.getIa() * (float) (-Math.cos(Math.PI / 2 + Math.toDegrees(Math.acos(m.getPfa())))));
            reactivePowerResponseHolder.setPhaseB(m.getVb() * m.getIb() * (float) (-Math.cos(Math.PI / 2 + Math.toDegrees(Math.acos(m.getPfb())))));
            reactivePowerResponseHolder.setPhaseC(m.getVc() * m.getIc() * (float) (-Math.cos(Math.PI / 2 + Math.toDegrees(Math.acos(m.getPfc())))));
            ReactivePowerResponse reactivePowerResponse = new ReactivePowerResponse();

            reactivePowerResponse.setCreateAt(m.getCreateAt());
            reactivePowerResponse.setReactivePowerA(reactivePowerResponseHolder.getPhaseA());
            reactivePowerResponse.setReactivePowerB(reactivePowerResponseHolder.getPhaseB());
            reactivePowerResponse.setReactivePowerC(reactivePowerResponseHolder.getPhaseC());
            reactivePowerResponse.setReactivePowerTotal(reactivePowerResponseHolder.getPhaseA() + reactivePowerResponseHolder.getPhaseB()+ reactivePowerResponseHolder.getPhaseC());
            reactivePowerResponseList.add(reactivePowerResponse);
        }*/

        return RestResp.createBySuccess(reactivePowerResponseList);
    }

    @Override
    public RestResp countFrequencyData(long createAt, GetElectricDataParam getElectricDataParam) {
        long remain = createAt + getRemainMillisSecondsOneDay(createAt);
        return this.countFrequencyDataRange(createAt, remain, getElectricDataParam);
    }

    @Override
    public RestResp countFrequencyDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam) {
        List<MeterRecord> meterRecordList = getMeterRecordList(startAt, endAt);
        List<FrequencyResponse> frequencyResponseList = new ArrayList<>(meterRecordList.size());


        for (MeterRecord m : meterRecordList) {
            FrequencyResponse frequencyResponse = new FrequencyResponse();
            frequencyResponse.setCreateAt(m.getCreateAt());
            frequencyResponse.setFrequency(m.getFrequency());
            frequencyResponseList.add(frequencyResponse);

        }
        return RestResp.createBySuccess(frequencyResponseList);
    }

    @Override
    public RestResp countDegreeOfThreePhaseUnbalanceData(long createAt, GetElectricDataParam getElectricDataParam) {
        long remain = createAt + getRemainMillisSecondsOneDay(createAt);
        return this.countDegreeOfThreePhaseUnbalanceDataRange(createAt, remain, getElectricDataParam);
    }

    @Override
    public RestResp countDegreeOfThreePhaseUnbalanceDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam) {
        List<MeterRecord> meterRecordList = getMeterRecordList(startAt, endAt);
        List<DegreeOfThreePhaseUnbalanceResponse> degreeOfThreePhaseUnbalanceResponseList = new ArrayList<>(meterRecordList.size());

        DegreeOfThreePhaseUnbalanceHolder degreeOfThreePhaseUnbalanceHolder = new DegreeOfThreePhaseUnbalanceHolder();

        float voltageTotal;
        float currentTotal;

        for (MeterRecord m : meterRecordList) {
            DegreeOfThreePhaseUnbalanceResponse degreeOfThreePhaseUnbalanceResponse = new DegreeOfThreePhaseUnbalanceResponse();
            degreeOfThreePhaseUnbalanceResponse.setCreateAt(m.getCreateAt());
            voltageTotal = m.getVa() + m.getVb() + m.getVc();
            degreeOfThreePhaseUnbalanceHolder.setUUnb((Math.max(Math.max(m.getVa(), m.getVb()), m.getVc()) - voltageTotal) / voltageTotal);

            currentTotal = m.getIa() + m.getIb() + m.getIc();
            degreeOfThreePhaseUnbalanceHolder.setIUnb((Math.max(Math.max(m.getIa(), m.getIb()), m.getIc()) - currentTotal) / currentTotal);


            degreeOfThreePhaseUnbalanceResponse.setUUnB(degreeOfThreePhaseUnbalanceHolder.getUUnb());
            degreeOfThreePhaseUnbalanceResponse.setIUnB(degreeOfThreePhaseUnbalanceHolder.getIUnb());
            degreeOfThreePhaseUnbalanceResponseList.add(degreeOfThreePhaseUnbalanceResponse);

        }
        return RestResp.createBySuccess(degreeOfThreePhaseUnbalanceResponseList);
    }

    @Override
    public RestResp countActivePowerMaxAvgMin(long startAt, long endAt) {
        /**
         * 结束日期为当天的最后一刻，即前端的参数可能为2019-02-17 10:00:00
         * 需要修正为2019-02-17 23:59:59，所以需要增加毫秒数
         */
        endAt = endAt + this.getRemainMillisSecondsOneDay(endAt);

        Timestamp start = new Timestamp(startAt);
        Timestamp end = new Timestamp(endAt);
        List<MaxAvgMinDto> maxAvgMinDtoList = meterRecordRepository.findMaxAvgMinByPower(start, end);
        return RestResp.createBySuccess(maxAvgMinDtoList);
    }

    @Override
    public RestResp producePhaseVoltageReport(long createAt, int minuteInterval) {
        long endAt = createAt + this.getRemainMillisSecondsOneDay(createAt);

        Timestamp start = new Timestamp(createAt);
        Timestamp end = new Timestamp(endAt);
        List<PhaseVoltageReportDto> phaseVoltageReportDtoList = meterRecordRepository.findByCreateAtInterval(start, end, minuteInterval);
        List<PhaseReportItem> reportItemList = new LinkedList<>();
        try {
            for (PhaseVoltageReportDto dto : phaseVoltageReportDtoList) {
                PhaseReportItem item = new PhaseReportItem();
                // 合并同名成员变量
                AutoAssembleUtil.assembleSameNameField(item, dto);
                // TODO 计算其他相关数据

                //添加到用于返回给前端的数据结构中
                reportItemList.add(item);
            }
            List<PhaseReportItem> phaseReportItems = PaddingTimeUtil.paddingZeroBy(reportItemList, minuteInterval, "CreateAt", PhaseReportItem.class);
            ReportResponse<PhaseReportItem> reportResponse = new ReportResponse<PhaseReportItem>();
            reportResponse.setInnerItemList(phaseReportItems);
            return RestResp.createBySuccess(reportResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestResp.createBySuccess(reportItemList);
    }

    private long getRemainMillisSecondsOneDay(long currentTimestamp) {
        LocalDateTime midnight = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimestamp),
                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimestamp),
                ZoneId.systemDefault());
        return ChronoUnit.MILLIS.between(currentDateTime, midnight);
    }

    private List<MeterRecord> getMeterRecordList(long startAt, long endAt) {
        return meterRecordRepository.findByCreateAtGreaterThanEqualAndCreateAtLessThanEqual(new Timestamp(startAt), new Timestamp(endAt));

    }


}

package power.api.service.impl;

import org.checkerframework.checker.units.qual.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import power.api.common.RestResp;
import power.api.controller.paramModel.GetElectricDataParam;
import power.api.controller.responseModel.powerMonitoring.*;
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

    @Override
    public RestResp countActivePowerDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam) {
        List<MeterRecord> meterRecordList = getMeterRecordList(startAt, endAt);
        List<ActivePowerResponse> activePowerResponsesList = new ArrayList<>(meterRecordList.size());
        for (MeterRecord m : meterRecordList) {
            ActivePowerResponse activePowerResponse = new ActivePowerResponse();
            activePowerResponse.setCreateAt(m.getCreateAt());
            activePowerResponse.setActivePower_total(m.getActivePower());
            activePowerResponsesList.add(activePowerResponse);

        }


        //注释不要删以后要用
/*       PhaseHolder activePowerHolder = new PhaseHolder();

        for (MeterRecord m : meterRecordList) {
            activePowerHolder.phase_a = m.getIa() * m.getVa() * m.getPfa();
            activePowerHolder.phase_b = m.getIb() * m.getVb() * m.getPfa();
            activePowerHolder.phase_c= m.getIc() * m.getVc() * m.getPfa();
            ActivePowerResponse activePowerResponse = new ActivePowerResponse();
            activePowerResponse.setCreateAt(m.getCreateAt());
            activePowerResponse.setActivePower_a(activePowerHolder.phase_a);

            activePowerResponse.setActivePower_b(activePowerHolder.phase_b);
            activePowerResponse.setActivePower_c(activePowerHolder.phase_c);

            activePowerResponse.setActivePower_total(activePowerHolder.phase_a + activePowerHolder.phase_b + activePowerHolder.phase_c);
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
            apparentPowerHolder.phase_a = m.getIa() * m.getVa();
            apparentPowerHolder.phase_b = m.getIb() * m.getVb();
            apparentPowerHolder.phase_c = m.getIc() * m.getVc();

            ApparentPowerResponse apparentPowerResponse = new ApparentPowerResponse();

            apparentPowerResponse.setCreateAt(m.getCreateAt());
            apparentPowerResponse.setApparentPower_a(apparentPowerHolder.phase_a);
            apparentPowerResponse.setApparentPower_b(apparentPowerHolder.phase_b);
            apparentPowerResponse.setApparentPower_c(apparentPowerHolder.phase_c);
            apparentPowerResponse.setApparentPower_total(apparentPowerHolder.phase_a + apparentPowerHolder.phase_b + apparentPowerHolder.phase_c);

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
            phaseCurrentResponse.setPhaseCurrent_a(m.getIa());
            phaseCurrentResponse.setPhaseCurrent_b(m.getIb());
            phaseCurrentResponse.setPhaseCurrent_c(m.getIc());
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
            phaseVoltageResponse.setPhaseVoltage_a(m.getVa());
            phaseVoltageResponse.setPhaseVoltage_b(m.getVb());
            phaseVoltageResponse.setPhaseVoltage_c(m.getVc());
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

        float Uab;
        float Ubc;
        float Uca;

        for (MeterRecord m : meterRecordList) {
            Uab = (float) (Math.sqrt(Math.pow(m.getVa(), 2)
                    + Math.pow(m.getVb(), 2)
                    - 2 * m.getVa() * m.getVb() * Math.cos(Math.PI * 2 / 3)));

            Ubc = (float) (Math.sqrt(Math.pow(m.getVb(), 2)
                    + Math.pow(m.getVc(), 2)
                    - 2 * m.getVb() * m.getVc() * Math.cos(Math.PI * 2 / 3)));

            Uca = (float) (Math.sqrt(Math.pow(m.getVc(), 2)
                    + Math.pow(m.getVa(), 2)
                    - 2 * m.getVc() * m.getVa() * Math.cos(Math.PI * 2 / 3)));


            LineVoltageResponse lineVoltageResponse = new LineVoltageResponse();

            lineVoltageResponse.setCreateAt(m.getCreateAt());
            lineVoltageResponse.setLineVoltage_ab(Uab);
            lineVoltageResponse.setLineVoltage_bc(Ubc);
            lineVoltageResponse.setLineVoltage_ca(Uca);
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
            powerFactorResponse.setPowerFactor_a(m.getPfa());
            powerFactorResponse.setPowerFactor_b(m.getPfb());
            powerFactorResponse.setPowerFactor_c(m.getPfc());
            powerFactorResponse.setPowerFactor_total(m.getPfa() + m.getPfb() + m.getPfc());
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

        PhaseHolder reactivePowerResponseHolder = new PhaseHolder();

        for (MeterRecord m : meterRecordList) {
            reactivePowerResponseHolder.phase_a = m.getVa() * m.getIa() * (float) (-Math.cos(Math.PI / 2 + Math.toDegrees(Math.acos(m.getPfa()))));
            reactivePowerResponseHolder.phase_b = m.getVb() * m.getIb() * (float) (-Math.cos(Math.PI / 2 + Math.toDegrees(Math.acos(m.getPfb()))));
            reactivePowerResponseHolder.phase_c = m.getVc() * m.getIc() * (float) (-Math.cos(Math.PI / 2 + Math.toDegrees(Math.acos(m.getPfc()))));
            ReactivePowerResponse reactivePowerResponse = new ReactivePowerResponse();

            reactivePowerResponse.setCreateAt(m.getCreateAt());
            reactivePowerResponse.setReactivePower_a(reactivePowerResponseHolder.phase_a);
            reactivePowerResponse.setReactivePower_b(reactivePowerResponseHolder.phase_b);
            reactivePowerResponse.setReactivePower_c(reactivePowerResponseHolder.phase_c);
            reactivePowerResponse.setReactivePower_total(reactivePowerResponseHolder.phase_a + reactivePowerResponseHolder.phase_b + reactivePowerResponseHolder.phase_c);
            reactivePowerResponseList.add(reactivePowerResponse);
        }

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


        float voltage_total=0f;
        float DegreeOfThreePhaseUnbalance_voltage;
        float current_total;
        float DegreeOfThreePhaseUnbalance_current;

        for (MeterRecord m : meterRecordList) {
            DegreeOfThreePhaseUnbalanceResponse degreeOfThreePhaseUnbalanceResponse = new DegreeOfThreePhaseUnbalanceResponse();
            degreeOfThreePhaseUnbalanceResponse.setCreateAt(m.getCreateAt());
            voltage_total = m.getVa() + m.getVb() + m.getVc();
            DegreeOfThreePhaseUnbalance_voltage = (Math.max(Math.max(m.getVa(), m.getVb()), m.getVc()) - voltage_total) / voltage_total;

            current_total = m.getIa() + m.getIb() + m.getIc();
            DegreeOfThreePhaseUnbalance_current = (Math.max(Math.max(m.getIa(), m.getIb()), m.getIc()) - current_total) / current_total;


            degreeOfThreePhaseUnbalanceResponse.setDegreeOfThreePhaseUnbalance_voltage(DegreeOfThreePhaseUnbalance_voltage);
            degreeOfThreePhaseUnbalanceResponse.setDegreeOfThreePhaseUnbalance_voltage(DegreeOfThreePhaseUnbalance_current);
            degreeOfThreePhaseUnbalanceResponseList.add(degreeOfThreePhaseUnbalanceResponse);

        }
        return RestResp.createBySuccess(degreeOfThreePhaseUnbalanceResponseList);
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

    class PhaseHolder {
        float phase_a, phase_b, phase_c;

    }
}

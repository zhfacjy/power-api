package power.api.service;

import power.api.controller.paramModel.GetElectricDataParam;
import power.api.common.RestResp;

/**
 * Created by 浩发 on 2019/2/4 13:46
 */
public interface IMeterRecordService {
    RestResp countActivePowerData(long createAt, GetElectricDataParam getElectricDataParam);

    RestResp countApparentPowerData(long createAt, GetElectricDataParam getElectricDataParam);

    RestResp countElectricEnergyData(long createAt, GetElectricDataParam getElectricDataParam);

    RestResp countElectricEnergyDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam);

    RestResp countTemperatureData(long createAt, GetElectricDataParam getElectricDataParam);

    RestResp countTemperatureDataRange(long startAt, long endAt, GetElectricDataParam getElectricDataParam);

    RestResp countPhaseCurrentData(long createAt, GetElectricDataParam getElectricDataParam);

    RestResp countPhaseVoltageData(long createAt, GetElectricDataParam getElectricDataParam);

    RestResp countLineVoltageData(long createAt, GetElectricDataParam getElectricDataParam);

    RestResp countPowerFactorData(long createAt, GetElectricDataParam getElectricDataParam);

    RestResp countReactivePowerData(long createAt, GetElectricDataParam getElectricDataParam);

    RestResp countFrequencyData(long createAt, GetElectricDataParam getElectricDataParam);

    RestResp countDegreeOfThreePhaseUnbalanceData(long createAt, GetElectricDataParam getElectricDataParam);

    RestResp countActivePowerMaxAvgMin(long startAt, long endAt);

    RestResp producePhaseVoltageReport(long createAt, int minuteInterval);

    RestResp producePowerLimitReport(long createAt, String createAtFormat, String sqlFormat);

    RestResp produceElectricCurrentLimitReport(long createAt, String createAtFormat, String sqlFormat);

    RestResp producePhaseVoltageLimitReport(long createAt, String createAtFormat, String sqlFormat);

    RestResp produceLineVoltageLimitReport(long createAt, String createAtFormat, String sqlFormat);
}

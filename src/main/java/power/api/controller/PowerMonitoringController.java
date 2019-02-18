package power.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import power.api.controller.paramModel.GetElectricDataParam;
import power.api.common.RestResp;
import power.api.controller.paramModel.GetRunningReportParam;
import power.api.service.IMeterRecordService;

@RestController
@Api(description = "电力监测")
@RequestMapping("/electric")
public class PowerMonitoringController {

    @Autowired
    private IMeterRecordService iMeterRecordService;

    /**
     * 电力监测 -> 电力数据 -> 日原始数据
     * http://www.acrelcloud.cn/SubstationWEB/ElectricData
     */
    @PostMapping("/data/{createAt}")
    public RestResp getElectricDataInADay(@ApiParam("日期（时间戳）") @PathVariable long createAt,
                                          @RequestBody GetElectricDataParam getElectricDataParam) {
        RestResp restResp = null;
        switch (getElectricDataParam.getElectricType()) {
            case "active_power":
                restResp = iMeterRecordService.countActivePowerData(createAt, getElectricDataParam);
                break;
            case "apparent_power":
                restResp = iMeterRecordService.countApparentPowerData(createAt, getElectricDataParam);
                break;
            case "electric_Energy":
                restResp = iMeterRecordService.countElectricEnergyData(createAt, getElectricDataParam);
                break;
            case "temperature":
                restResp = iMeterRecordService.countTemperatureData(createAt, getElectricDataParam);
                break;
            case "phase_current":
                restResp = iMeterRecordService.countPhaseCurrentData(createAt, getElectricDataParam);
                break;
            case "phase_voltage":
                restResp = iMeterRecordService.countPhaseVoltageData(createAt, getElectricDataParam);
                break;
            case "line_voltage":
                restResp = iMeterRecordService.countLineVoltageData(createAt, getElectricDataParam);
                break;
            case "power_factor":
                restResp = iMeterRecordService.countPowerFactorData(createAt, getElectricDataParam);
                break;
            case "reactive_power":
                restResp = iMeterRecordService.countReactivePowerData(createAt, getElectricDataParam);
                break;
            case "frequency":
                restResp = iMeterRecordService.countFrequencyData(createAt, getElectricDataParam);
                break;
            case "degree_of_three_phase_unbalance":
                restResp = iMeterRecordService.countDegreeOfThreePhaseUnbalanceData(createAt, getElectricDataParam);
                break;
            default:
                restResp = RestResp.createBy(RestResp.PARAM_ERROR, "参数错误");
        }
        return restResp;
    }

    @PostMapping("/data/{startAt}/{endAt}")
    public RestResp getMaxAvgMinElectricData(@ApiParam("起始日期（时间戳）") @PathVariable Long startAt,
                                             @ApiParam("结束日期（时间戳）") @PathVariable Long endAt,
                                             @RequestBody GetElectricDataParam getElectricDataParam) {
        RestResp restResp = null;
        switch (getElectricDataParam.getElectricType()) {
            case "active_power":
                restResp = iMeterRecordService.countActivePowerMaxAvgMin(startAt, endAt);
                break;
            default:
                restResp = RestResp.createBy(RestResp.PARAM_ERROR, "参数错误");
        }
        return restResp;
    }

    @PostMapping("/running/report/{createAt}")
    public RestResp getRunningReport(@ApiParam("指定日期（时间戳）") @PathVariable Long createAt,
                                     @RequestBody GetRunningReportParam getRunningReportParam) {
        RestResp restResp = null;
        if (getRunningReportParam.getMinuteInterval() <= 0) {
            return RestResp.createBy(RestResp.PARAM_ERROR, "参数错误");
        }
        switch (getRunningReportParam.getReportType()) {
            case "phase_voltage":
                restResp = iMeterRecordService.producePhaseVoltageReport(createAt, getRunningReportParam.getMinuteInterval());
                break;
            case "line_voltage":
                restResp = RestResp.createBy(RestResp.INVISIBLE, "开发中...");
                break;
            case "all":
                restResp = RestResp.createBy(RestResp.INVISIBLE, "开发中...");
                break;
            default:
                restResp = RestResp.createBy(RestResp.PARAM_ERROR, "参数错误");
        }
        return restResp;
    }
}

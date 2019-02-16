package power.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import power.api.controller.paramModel.GetElectricDataParam;
import power.api.dto.RestResp;
import power.api.service.IMeterRecordService;

import java.sql.Timestamp;
import java.util.Date;

@RestController
@Api(description = "电力监测")
@RequestMapping("/electric")
public class PowerMonitoringController {

    @Autowired
    private IMeterRecordService iMeterRecordService;

    /**
     * ElectricData
     */
    @PostMapping("/data/{createAt}")
    public RestResp getElectricDataInADay(@ApiParam("日期（时间戳）") @PathVariable long createAt,
                                          @RequestBody GetElectricDataParam getElectricDataParam) {
        RestResp restResp = null;
        switch (getElectricDataParam.getElectricType()) {
            case "active_power":
                restResp = iMeterRecordService.countActivePowerData(createAt, getElectricDataParam);
                break;
            case "electric_current":
                // TODO 计算电流
                break;
            default:
                restResp = RestResp.createBy(RestResp.PARAM_ERROR, "参数错误");
        }
        return restResp;
    }

    @PostMapping("/data/{startAt}/{endAt}")
    public RestResp getElectricDataInRangeDay(@ApiParam("起始日期（时间戳）") @PathVariable Long startAt,
                                              @ApiParam("结束日期（时间戳）") @PathVariable Long endAt,
                                              @RequestBody GetElectricDataParam getElectricDataParam) {
        return RestResp.createBySuccess();
    }
}

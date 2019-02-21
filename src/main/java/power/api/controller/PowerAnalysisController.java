package power.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import power.api.common.RestResp;
import power.api.service.IPowerAnalysisService;

/**
 * Created by 浩发 on 2019/2/17 15:41
 * 用电分析
 */
@RestController
@Api(description = "用电分析")
@RequestMapping("/analysis")
public class PowerAnalysisController {

    @Autowired
    private IPowerAnalysisService powerAnalysisService;

    /**
     * 用能分析
     * @param createAt
     * @param dataType
     * @return
     */
    @GetMapping("/power/{dataType}/{createAt}")
    public RestResp getElectricDataInADay(@ApiParam("日期（根据日期类型的字符串类型: 1:yyyyMMdd 2:yyyyMM 3:yyyy）") @PathVariable String createAt,
                                          @ApiParam("日期类型（3.年、2.月、1.日）") @PathVariable Integer dataType) {
        return RestResp.createBySuccess(powerAnalysisService.getPowerAnalysis(dataType, createAt));
    }

    /**
     * 同比分析
     * @param centralNode
     * @return
     */
    @GetMapping("/power/yearOnYear/{centralNode}")
    public RestResp yearOnYear(@ApiParam("中心节点（默认传04）") @PathVariable String centralNode) {
        return RestResp.createBySuccess(powerAnalysisService.yearOnYear(centralNode));
    }

    /**
     * 环比分析
     * @param createAt
     * @param dataType
     * @return
     */
    @GetMapping("/power/monthOnMonth/{centralNode}/{dataType}/{createAt}")
    public RestResp monthOnMonth(@ApiParam("日期（字符串类型:yyyyMMdd）") @PathVariable String createAt,
                                 @ApiParam("日期类型（3.年、2.月、1.日）") @PathVariable Integer dataType,
                                 @ApiParam("中心节点（默认传04）") @PathVariable String centralNode) {
        return RestResp.createBySuccess(powerAnalysisService.monthOnMonth(centralNode,dataType,createAt));
    }

}

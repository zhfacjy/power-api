package power.api.controller.paramModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "获取电力极值报表的请求参数说明")
public class GetLimitReportParam {
    @ApiModelProperty("电压类别，参数：\n" +
            "功率：power\n" +
            "电流：electric_current\n" +
            "相电压：phase_voltage\n" +
            "线电压：line_voltage")
    private String reportType;
}

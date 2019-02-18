package power.api.controller.paramModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "获取电力运行报表的请求参数说明")
public class GetRunningReportParam {
    @ApiModelProperty("电压类别，参数：\n" +
            "相电压：phase_voltage\n" +
            "线电压：line_voltage\n" +
            "全部：all")
    private String reportType;
    @ApiModelProperty("间隔时间，单位：分钟")
    private Integer minuteInterval;
}

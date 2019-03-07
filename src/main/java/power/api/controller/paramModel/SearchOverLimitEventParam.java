package power.api.controller.paramModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel(description = "查询越限事件参数")
public class SearchOverLimitEventParam {
    @ApiModelProperty("开始时间")
    private long beginDate;
    @ApiModelProperty("结束时间")
    private long endDate;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("仪表名称")
    private String meter;
}

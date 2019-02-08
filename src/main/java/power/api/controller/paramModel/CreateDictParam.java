package power.api.controller.paramModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "创建字典的参数")
public class CreateDictParam {
    @ApiModelProperty("字典描述")
    private String label;
    @ApiModelProperty("字典值")
    private String value;
    @ApiModelProperty("父id，最顶级则为0")
    private Integer parentId;
    @ApiModelProperty("      1.meter(电表)\n" +
            "      2.line_counters（集线柜，多层）\n" +
            "      3.patrol_group（巡检组别）\n" +
            "      4.patrol_category（巡视类别）\n" +
            "      5.patrol_nature（巡视性质）\n" +
            "      5.plan_status（巡视执行状态）")
    private String type;

    @Override
    public String toString() {
        return "CreateDictParam{" +
                "label='" + label + '\'' +
                ", value='" + value + '\'' +
                ", parentId=" + parentId +
                ", type='" + type + '\'' +
                '}';
    }
}

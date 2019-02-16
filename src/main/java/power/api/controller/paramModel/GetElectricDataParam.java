package power.api.controller.paramModel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "电力数据中的的请求参数")
public class GetElectricDataParam {
    @ApiModelProperty("电力类别，参数：\n" +
            "有功功率：active_power\n" +
            "电流：electric_current\n" +
            "相电压：phase_voltage\n" +
            "线电压：line_voltage\n" +
            "功率因数：power_factor\n" +
            "无功率功率：reactive_power\n" +
            "视在功率：apparent_power")
    private String electricType;
    @ApiModelProperty("A相，true 或 false，type为active_power、electric_current、phase_voltage时有效")
    private Boolean phaseA;
    @ApiModelProperty("B相，true 或 false，type为active_power、electric_current、phase_voltage时有效")
    private Boolean phaseB;
    @ApiModelProperty("C相，true 或 false，type为active_power、electric_current、phase_voltage时有效")
    private Boolean phaseC;
    @ApiModelProperty("三相求和，true 或 false，type为active_power、power_factor、reactive_power、apparent_power时有效")
    private Boolean total;
    @ApiModelProperty("线电压中的选项，true 或 false，type为line_voltage时有效")
    private Boolean uab;
    @ApiModelProperty("线电压中的选项，true 或 false，type为line_voltage时有效")
    private Boolean ubc;
    @ApiModelProperty("线电压中的选项，true 或 false，type为line_voltage时有效")
    private Boolean uca;
}

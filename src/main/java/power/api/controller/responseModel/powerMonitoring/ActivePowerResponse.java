package power.api.controller.responseModel.powerMonitoring;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import power.api.util.CustomerDoubleSerialize;

import java.sql.Timestamp;

/**
 * 计算功率后，返回给前端的数据结构定义
 * 这是为了减少多余字段，仅返回给前端需要的字段
 * <p>
 * 在功率中前端只需要时间、ABC相的功率、总功率
 */
@Data
public class ActivePowerResponse {

    private Timestamp createAt;
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double activePowerA;
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double activePowerB;
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double activePowerC;
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private double activePowerTotal;

    public ActivePowerResponse() {
    }
}

package power.api.controller.responseModel.powerMonitoring;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 计算功率后，返回给前端的数据结构定义
 * 这是为了减少多余字段，仅返回给前端需要的字段
 *
 * 在功率中前端只需要时间、ABC相的功率、总功率
 */
@Data
public class DegreeOfThreePhaseUnbalanceResponse {
    private Timestamp createAt;

    private float IUnB; //电流
    private float UUnB; //电压


    public DegreeOfThreePhaseUnbalanceResponse() {
    }
}

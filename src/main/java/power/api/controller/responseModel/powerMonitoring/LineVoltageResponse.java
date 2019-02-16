package power.api.controller.responseModel.powerMonitoring;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class LineVoltageResponse {
    private Timestamp createAt;
    private float lineVoltage_ab;
    private float lineVoltage_bc;
    private float lineVoltage_ca;
    private double lineVoltage_total;

    public LineVoltageResponse() {
    }
}

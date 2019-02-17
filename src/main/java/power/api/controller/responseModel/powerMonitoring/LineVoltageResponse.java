package power.api.controller.responseModel.powerMonitoring;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class LineVoltageResponse {
    private Timestamp createAt;
    private float Uab;
    private float Ubc;
    private float Uca;

    public LineVoltageResponse() {
    }
}

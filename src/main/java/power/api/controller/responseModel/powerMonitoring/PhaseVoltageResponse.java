package power.api.controller.responseModel.powerMonitoring;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PhaseVoltageResponse {
    private Timestamp createAt;
    private float phaseVoltageA;
    private float phaseVoltageB;
    private float phaseVoltageC;

    public PhaseVoltageResponse() {
    }
}

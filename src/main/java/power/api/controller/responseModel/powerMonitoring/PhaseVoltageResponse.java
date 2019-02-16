package power.api.controller.responseModel.powerMonitoring;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PhaseVoltageResponse {
    private Timestamp createAt;
    private float phaseVoltage_a;
    private float phaseVoltage_b;
    private float phaseVoltage_c;

    public PhaseVoltageResponse() {
    }
}

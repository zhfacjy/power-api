package power.api.controller.responseModel.powerMonitoring;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PhaseCurrentResponse {
    private Timestamp createAt;
    private float phaseCurrentA;
    private float phaseCurrentB;
    private float phaseCurrentC;

    public PhaseCurrentResponse() {
    }
}

package power.api.controller.responseModel.powerMonitoring;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PhaseCurrentResponse {
    private Timestamp createAt;
    private double phaseCurrentA;
    private double phaseCurrentB;
    private double phaseCurrentC;

    public PhaseCurrentResponse() {
    }
}

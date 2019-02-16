package power.api.controller.responseModel.powerMonitoring;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PhaseCurrentResponse {
    private Timestamp createAt;
    private double phaseCurrent_a;
    private double phaseCurrent_b;
    private double phaseCurrent_c;

    public PhaseCurrentResponse() {
    }
}

package power.api.controller.responseModel.powerMonitoring;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PowerFactorResponse {
    private Timestamp createAt;
    private float powerFactor_a;
    private float powerFactor_b;
    private float powerFactor_c;
    private double PowerFactor_total;

    public PowerFactorResponse() {
    }
}

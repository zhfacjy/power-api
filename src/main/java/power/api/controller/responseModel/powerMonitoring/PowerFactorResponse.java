package power.api.controller.responseModel.powerMonitoring;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PowerFactorResponse {
    private Timestamp createAt;
    private float powerFactorA;
    private float powerFactorB;
    private float powerFactorC;
    private double PowerFactorTotal;

    public PowerFactorResponse() {
    }
}

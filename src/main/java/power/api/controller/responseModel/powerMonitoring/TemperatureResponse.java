package power.api.controller.responseModel.powerMonitoring;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TemperatureResponse {
    private Timestamp createAt;
    private float Temperature;

    public TemperatureResponse() {
    }
}

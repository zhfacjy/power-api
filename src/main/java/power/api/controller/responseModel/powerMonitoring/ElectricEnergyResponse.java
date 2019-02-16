package power.api.controller.responseModel.powerMonitoring;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ElectricEnergyResponse {
    private Timestamp createAt;
    private double electricEnergy;

    public ElectricEnergyResponse() {
    }
}

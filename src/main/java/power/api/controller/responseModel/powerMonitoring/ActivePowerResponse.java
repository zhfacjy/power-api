package power.api.controller.responseModel.powerMonitoring;

import lombok.Data;
import power.api.dto.ActivePower;

import java.util.List;

@Data
public class ActivePowerResponse {
    private List<ActivePower> activePowerList;
}

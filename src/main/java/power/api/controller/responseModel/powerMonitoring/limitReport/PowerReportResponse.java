package power.api.controller.responseModel.powerMonitoring.limitReport;

import java.util.List;

public class PowerReportResponse {
    List<PowerReportItem> activePower;
    List<PowerReportItem> reactivePower;
    List<PowerReportItem> ApparentPower;
}

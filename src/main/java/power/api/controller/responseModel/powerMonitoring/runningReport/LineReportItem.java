package power.api.controller.responseModel.powerMonitoring.runningReport;

import lombok.Data;

@Data
public class LineReportItem {
    private String createAt;
    private Double ia, ib, ic, p, q, pf;
    private Integer epi;
    private Double uab, ubc, uca;
}

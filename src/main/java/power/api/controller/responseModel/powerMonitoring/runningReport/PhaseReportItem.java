package power.api.controller.responseModel.powerMonitoring.runningReport;

import lombok.Data;

@Data
public class PhaseReportItem {
    private String createAt;
    private Double ia, ib, ic, p, q, pf;
    private Integer epi;
    private Double ua, ub, uc;

    @Override
    public String toString() {
        return "PhaseReportItem{" +
                "createAt='" + createAt + '\'' +
                ", ia=" + ia +
                ", ib=" + ib +
                ", ic=" + ic +
                ", p=" + p +
                ", q=" + q +
                ", pf=" + pf +
                ", epi=" + epi +
                ", ua=" + ua +
                ", ub=" + ub +
                ", uc=" + uc +
                '}';
    }
}

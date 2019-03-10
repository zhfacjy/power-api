package power.api.controller.responseModel.powerMonitoring.runningReport;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import power.api.util.CustomerDoubleSerialize;

@Data
public class PhaseReportItem {
    private String createAt;
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double ia, ib, ic, p, q, pf;
    private Integer epi;
    @JsonSerialize(using = CustomerDoubleSerialize.class)
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

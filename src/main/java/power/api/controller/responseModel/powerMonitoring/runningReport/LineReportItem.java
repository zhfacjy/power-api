package power.api.controller.responseModel.powerMonitoring.runningReport;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import power.api.util.CustomerDoubleSerialize;

@Data
public class LineReportItem {
    private String createAt;
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double ia, ib, ic, p, q, pf;
    private Integer epi;
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double uab, ubc, uca;
}

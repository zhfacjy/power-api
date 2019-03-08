package power.api.controller.responseModel.powerMonitoring.limitReport;

import lombok.Data;

import java.util.List;

@Data
public class LimitMaxAvgMin {
    private String listDesc;
    private List<LimitMaxAvgMinItem> list;
}

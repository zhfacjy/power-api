package power.api.controller.responseModel.powerMonitoring.runningReport;

import lombok.Data;

import java.util.List;

@Data
public class ReportResponse<T> {

    private List<T> innerItemList;
    private T minItem;
    private T avgItem;
    private T maxItem;
}

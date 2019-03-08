package power.api.controller.responseModel.powerMonitoring.limitReport;

import lombok.Data;

@Data
public class LimitMaxAvgMinItem {
    private String meter;
    private String maxValueCreateAt;
    private String minValueCreateAt;
    private Double maxValue;
    private Double avgValue;
    private Double minValue;
}

package power.api.controller.responseModel.powerMonitoring.limitReport;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import power.api.util.CustomerDoubleSerialize;

@Data
public class LimitMaxAvgMinItem {
    private String meter;
    private String maxValueCreateAt;
    private String minValueCreateAt;
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double maxValue;
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double avgValue;
    @JsonSerialize(using = CustomerDoubleSerialize.class)
    private Double minValue;
}

package power.api.controller.responseModel.powerMonitoring.limitReport;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * {
 * meter:'01',
 * createAt:'2019-02-20',
 * maxValue:23,
 * maxValueCreateAt:'08:30',
 * minValue:11,
 * minValueCreateAt:'02:20',
 * avgValue:22
 * }
 */
@Data
public class PowerReportResponse {
    private String meter;
    private String createAt;

    private List<LimitMaxAvgMin> list;
}

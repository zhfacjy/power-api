package power.api.controller.responseModel.powerMonitoring.limitReport;

import lombok.Data;

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
    private String maxValueCreateAt;
    private String minValueCreateAt;
    private Double maxValue;
    private Double minValue;
    private Double avgValue;
}

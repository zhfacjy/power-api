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

    private String maxActivePowerCreateAt;
    private String minActivePowerCreateAt;
    private Double maxActivePower;
    private Double minActivePower;
    private Double avgActivePower;

    private String maxReactivePowerCreateAt;
    private String minReactivePowerCreateAt;
    private Double maxReactivePower;
    private Double minReactivePower;
    private Double avgReactivePower;

    private String maxApparentPowerCreateAt;
    private String minApparentPowerCreateAt;
    private Double maxApparentPower;
    private Double minApparentPower;
    private Double avgApparentPower;
}

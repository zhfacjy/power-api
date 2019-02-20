package power.api.controller.responseModel.powerMonitoring.limitReport;

import lombok.Data;

@Data
public class LineVoltageReportResponse {
    private String meter;
    private String createAt;

    private String maxUabCreateAt;
    private String minUabCreateAt;
    private Double maxUab;
    private Double minUab;
    private Double avgUab;

    private String maxUbcCreateAt;
    private String minUbcCreateAt;
    private Double maxUbc;
    private Double minUbc;
    private Double avgUbc;

    private String maxUcaCreateAt;
    private String minUcaCreateAt;
    private Double maxUca;
    private Double minUca;
    private Double avgUca;

    public LineVoltageReportResponse(String meter, String createAt) {
        this.meter = meter;
        this.createAt = createAt;
    }
}

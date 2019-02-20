package power.api.controller.responseModel.powerMonitoring.limitReport;

import lombok.Data;

@Data
public class PhaseVoltageReportResponse {
    private String meter;
    private String createAt;

    private String maxUaCreateAt;
    private String minUaCreateAt;
    private Double maxUa;
    private Double minUa;
    private Double avgUa;

    private String maxUbCreateAt;
    private String minUbCreateAt;
    private Double maxUb;
    private Double minUb;
    private Double avgUb;

    private String maxUcCreateAt;
    private String minUcCreateAt;
    private Double maxUc;
    private Double minUc;
    private Double avgUc;

    public PhaseVoltageReportResponse(String meter, String createAt) {
        this.meter = meter;
        this.createAt = createAt;
    }
}

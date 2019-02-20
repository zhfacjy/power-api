package power.api.controller.responseModel.powerMonitoring.limitReport;

import lombok.Data;

@Data
public class ElectricCurrentReportResponse {
    private String meter;
    private String createAt;

    private String maxIaCreateAt;
    private String minIaCreateAt;
    private Double maxIa;
    private Double minIa;
    private Double avgIa;

    private String maxIbCreateAt;
    private String minIbCreateAt;
    private Double maxIb;
    private Double minIb;
    private Double avgIb;

    private String maxIcCreateAt;
    private String minIcCreateAt;
    private Double maxIc;
    private Double minIc;
    private Double avgIc;

    public ElectricCurrentReportResponse(String meter, String createAt) {
        this.meter = meter;
        this.createAt = createAt;
    }
}

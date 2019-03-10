package power.api.util;

import org.junit.Test;
import power.api.controller.responseModel.powerMonitoring.runningReport.LineReportItem;
import power.api.controller.responseModel.powerMonitoring.runningReport.PhaseReportItem;
import power.api.dto.MeterRecordDto;
import power.api.model.MeterRecord;

import static org.junit.Assert.*;

public class AutoAssembleUtilTest {
    @Test
    public void assembleSameNameField() throws Exception {
        PhaseReportItem phaseReportItem = new PhaseReportItem();
        LineReportItem lineReportItem = new LineReportItem();
        double checkValue = 22.22;
        lineReportItem.setIa(checkValue);
        AutoAssembleUtil.assembleSameNameField(phaseReportItem, lineReportItem);
        assertEquals(checkValue, phaseReportItem.getIa(), 0);
        MeterRecord meterRecord = new MeterRecord();
        MeterRecordDto meterRecordDto = new MeterRecordDto() {
            @Override
            public Float getUa() {
                return 12.1f;
            }

            @Override
            public Float getUb() {
                return 12.2f;
            }

            @Override
            public Float getUc() {
                return 12.3f;
            }

            @Override
            public Float getIa() {
                return 11.1f;
            }

            @Override
            public Float getIb() {
                return 11.2f;
            }

            @Override
            public Float getIc() {
                return 11.3f;
            }

            @Override
            public Float getPfa() {
                return null;
            }

            @Override
            public Float getPfb() {
                return null;
            }

            @Override
            public Float getPfc() {
                return null;
            }

            @Override
            public Double getActivePower() {
                return null;
            }

            @Override
            public Integer getTemperature() {
                return null;
            }

            @Override
            public Integer getCurrentLimit() {
                return null;
            }

            @Override
            public Double getFrequency() {
                return null;
            }

            @Override
            public Double getElectricEnergy() {
                return null;
            }

            @Override
            public String getCreateAt() {
                return null;
            }
        };
        AutoAssembleUtil.assembleSameNameField(meterRecord, meterRecordDto);
        assertEquals(meterRecord.getUa(), meterRecordDto.getUa(), 0);
    }

}
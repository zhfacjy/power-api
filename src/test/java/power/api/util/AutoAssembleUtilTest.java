package power.api.util;

import org.junit.Test;
import power.api.controller.responseModel.powerMonitoring.runningReport.LineReportItem;
import power.api.controller.responseModel.powerMonitoring.runningReport.PhaseReportItem;

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
    }

}
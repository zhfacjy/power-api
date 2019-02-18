package power.api.util;

import org.junit.Test;
import power.api.dto.PhaseVoltageReportDto;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class PaddingTimeUtilTest {
    /**
     * 测试
     * 已经获得每15分钟统计一次的数据，测试补充没有数据的区间
     *
     * @throws Exception
     */
    @Test
    public void paddingZeroByCreateAT() throws Exception {
        List<SimTest> simTests = new LinkedList<>();

        String date1 = "2019-02-17 10:15:00";
        double date1Value = 22.2;

        String date2 = "2019-02-17 10:30:00";
        double date2Value = 11.1;

        simTests.add(new SimTest(date1, date1Value));
        simTests.add(new SimTest(date2, date2Value));
        List<SimTest> simTestList = PaddingTimeUtil.paddingZeroBy(simTests, 15, "CreateAt", SimTest.class);
        for (SimTest simTest : simTestList) {
            System.out.println(simTest);
            if (simTest.getCreateAt().equals(date1)) {
                assertEquals(date1Value, simTest.getV(), 0);
            } else if (simTest.getCreateAt().equals(date2)) {
                assertEquals(date2Value, simTest.getV(), 0);
            } else {
                assertEquals(0.0, simTest.getV(), 0);
            }
        }

    }

}

class SimTest {
    double v;
    String createAt;

    public SimTest(String createAt, double v) {
        this.createAt = createAt;
        this.v = v;
    }

    public SimTest() {
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "SimTest{" +
                "v=" + v +
                ", createAt='" + createAt + '\'' +
                '}';
    }
}
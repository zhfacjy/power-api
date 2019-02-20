package power.api.util.meterRecordCalculator;

import power.api.model.MeterRecord;

public interface IMeterRecordCalculator {


    /**
     * 根据MeterRecord的字段计算无功功率
     *
     * @param meterRecord
     * @param three       是否三相
     * @return
     */
    float countReactivePower(MeterRecord meterRecord, Boolean three);


    /**
     * 根据MeterRecord的字段计算视在功率
     *
     * @param meterRecord
     * @param three       是否三相
     * @return
     */
    float countApparentPower(MeterRecord meterRecord, Boolean three);

    /**
     * 计算线电压
     *
     * @param arg1 电压1
     * @param arg2 电压2
     * @return
     */
    float countLineVoltage(double arg1, double arg2);
}

package power.api.util.meterRecordCalculator;

import power.api.model.MeterRecord;

/**
 * 这个接口类用于定义需要计算的方法接口，由MeterRecordCalculator类实现
 *
 * 增加计算方法步骤：
 * 1.在这个接口类添加一个接口
 * 2.在实现类实现该接口
 * 3.在实现类的底部增加static方法，通过instance调用这些接口
 */
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

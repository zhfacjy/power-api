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
     * @return 三个数之和
     */
    float sumOfThreeNums(double arg0,double arg1,double arg2);

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @return 返回三个数平均值
     */
    float averageOfThreeNums(double arg0,double arg1,double arg2);

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @return 返回三个数中的最大值
     */
    float maxOfThreeNums(double arg0,double arg1,double arg2);
    /**
     * @param cosX
     * @return cosx转换sinx后的值
     */
    float cosXtoSinX(double cosX);


    /**
     * @param voltage
     * @param current
     * @return 返回视在功率计算值
     */
    float countApparentPower(double voltage,double current);


    /**
     * @param apparentPower
     * @param cosX
     * @return 返回无功功率计算值
     */
    float countReactivePower(float apparentPower,double cosX);

    /**
     * @param apparentPower
     * @param cosX
     * @return 返回有功功率计算值
     */
    float countActivePower(float apparentPower,double cosX);

    /**
     * @param max
     * @param average
     * @return 返回三相不平衡度计算值
     */
    float countThreePhaseUnbalanced(float max,float average);

    /**
     * @param voltage0
     * @param voltage1
     * @return 返回两个相电压之间的线电压
     */
    float countLineVoltage(double voltage0,double voltage1);

}

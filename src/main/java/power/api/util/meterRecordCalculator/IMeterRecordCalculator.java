package power.api.util.meterRecordCalculator;

import power.api.model.MeterRecord;

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

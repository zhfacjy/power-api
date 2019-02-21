package power.api.util.meterRecordCalculator;

import org.hibernate.internal.util.MathHelper;
import power.api.model.MeterRecord;

/**
 * 这个接口类用于实现需要计算的方法接口，继承自IMeterRecordCalculator接口类
 * <p>
 * 增加计算方法的步骤：
 * 1.在接口类IMeterRecordCalculator添加一个接口
 * 2.在当前类实现该接口
 * 3.在当前类的底部增加static方法，通过instance调用这些接口
 */
public class MeterRecordCalculator implements IMeterRecordCalculator {

    private static MeterRecordCalculator instance = null;

    static {
        instance = instance == null ? new MeterRecordCalculator() : instance;
    }

    private MeterRecordCalculator(){}

    @Override
     public  float sumOfThreeNums(double arg0, double arg1, double arg2) {
        return (float) (arg0+arg1+arg1);
    }

    @Override
     public  float averageOfThreeNums(double arg0, double arg1, double arg2) {
        return (float) ((arg0+arg1+arg1)/3);
    }

    @Override
     public  float maxOfThreeNums(double arg0, double arg1, double arg2) {
        return (float) ((((arg0>arg1)?arg0:arg1)>arg2)?((arg0>arg1)?arg0:arg1):arg2);
    }

    @Override
     public  float cosXtoSinX(double cosX) {
        return (float) (-Math.cos(Math.PI/2+Math.acos(cosX)));
    }

    @Override
     public  float countApparentPower(double voltage, double current) {
        return (float) (voltage*current);
    }

    @Override
     public  float countReactivePower(float apparentPower, double cosX) {
        return apparentPower*cosXtoSinX(cosX);
    }

    @Override
     public  float countActivePower(float apparentPower, double cosX) {
        return (float) (apparentPower*cosX);
    }

    @Override
     public  float countThreePhaseUnbalanced(float max, float average) {
        return (max-average)/average;
    }

    @Override
     public  float countLineVoltage(double arg1, double arg2) {
        return (float) (Math.sqrt(Math.pow(arg1, 2) + Math.pow(arg2, 2) - 2 * arg1 * arg2* Math.cos(Math.PI * 2 / 3)));

    }


    /**
     * @param meterRecord
     * @return 三相电压之和
     */
     public static  float countPhaseVoltageTotal(MeterRecord meterRecord) {
            return instance.sumOfThreeNums(meterRecord.getUa() , meterRecord.getUb() , meterRecord.getUc());
    }
    /**
     * @param meterRecord
     * @return 三相电流之和
     */
     public static  float countPhaseCurrentTotal(MeterRecord meterRecord) {
            return instance.sumOfThreeNums(meterRecord.getIa() , meterRecord.getIb() , meterRecord.getIc());
    }
    /**
     * @param meterRecord
     * @return 三相功率因数之和
     */
     public static  float countPowerFactorTotal(MeterRecord meterRecord) {
            return instance.sumOfThreeNums(meterRecord.getPfa() , meterRecord.getPfb() , meterRecord.getPfc());
    }

    //视在功率
     public static  float countApparentPowerA(MeterRecord meterRecord) {
        return instance.countApparentPower(meterRecord.getUa(),meterRecord.getIa());

    }

     public static  float countApparentPowerB(MeterRecord meterRecord) {
        return instance.countApparentPower(meterRecord.getUb(),meterRecord.getIb());

    }

     public static  float countApparentPowerC(MeterRecord meterRecord) {
        return instance.countApparentPower(meterRecord.getUc(),meterRecord.getIc());

    }

     public static  float countApparentPowerTotal(MeterRecord meterRecord) {
        return instance.countApparentPowerA(meterRecord)+countApparentPowerB(meterRecord)+countApparentPowerC(meterRecord);

    }

     /**
      * 计算Uab
      *
      * @param meterRecord
      * @return
      */
     public static float countLineVoltageUab(MeterRecord meterRecord) {
         return instance.countLineVoltage(meterRecord.getUa(), meterRecord.getUb());
     }

     /**
      * 计算Ubc
      *
      * @param meterRecord
      * @return
      */
     public static float countLineVoltageUbc(MeterRecord meterRecord) {
         return instance.countLineVoltage(meterRecord.getUb(), meterRecord.getUc());
     }

     /**
      * 计算Uca
      *
      * @param meterRecord
      * @return
      */
     public static float countLineVoltageUca(MeterRecord meterRecord) {
         return instance.countLineVoltage(meterRecord.getUc(), meterRecord.getUa());
     }

    //有功功率
     public static  float countActivePowerA(MeterRecord meterRecord) {
        return instance.countActivePower(countApparentPowerA(meterRecord),meterRecord.getPfa());

    }
     public static  float countActivePowerB(MeterRecord meterRecord) {
        return instance.countActivePower(countApparentPowerB(meterRecord),meterRecord.getPfb());

    }
     public static  float countActivePowerC(MeterRecord meterRecord) {
        return instance.countActivePower(countApparentPowerC(meterRecord),meterRecord.getPfc());

    }

     public static  float countActivePowerTotal(MeterRecord meterRecord) {
        return instance.countActivePower(countApparentPowerTotal(meterRecord),countPowerFactorTotal(meterRecord));

    }

    //无功功率
     public static  float countReactivePowerA(MeterRecord meterRecord) {
        return instance.countReactivePower(countReactivePowerA(meterRecord),meterRecord.getPfa());

    }
     public static  float countReactivePowerB(MeterRecord meterRecord) {
        return instance.countReactivePower(countReactivePowerB(meterRecord),meterRecord.getPfb());

    }
     public static  float countReactivePowerC(MeterRecord meterRecord) {
        return instance.countReactivePower(countReactivePowerC(meterRecord),meterRecord.getPfc());

    }


     public static  float countReactivePowerTotal(MeterRecord meterRecord) {
        return instance.countReactivePower(instance.countApparentPowerTotal(meterRecord),countPowerFactorTotal(meterRecord));
    }

    //暂时性方法
     public static  float countReactivePowerTotalTest(MeterRecord meterRecord) {
         return instance.countReactivePower(instance.countApparentPowerTotal(meterRecord),meterRecord.getActivePower()/instance.countApparentPowerTotal(meterRecord));
     }


     /**
      * @param meterRecord
      * @return 电流三相不平衡度
      */
     public static  float countCurrentThreePhaseUnbalanced(MeterRecord meterRecord) {
        return instance.countThreePhaseUnbalanced(instance.maxOfThreeNums(meterRecord.getIa(),meterRecord.getIb(),meterRecord.getIc())
                ,instance.averageOfThreeNums(meterRecord.getIa(),meterRecord.getIb(),meterRecord.getIc()));

    }

     /**
      * @param meterRecord
      * @return 电压三相不平衡度
      */
     public static  float countVoltageThreePhaseUnbalanced(MeterRecord meterRecord) {
        return instance.countThreePhaseUnbalanced(instance.maxOfThreeNums(meterRecord.getUa(),meterRecord.getUb(),meterRecord.getUc())
                ,instance.averageOfThreeNums(meterRecord.getUa(),meterRecord.getUb(),meterRecord.getUc()));

    }




}

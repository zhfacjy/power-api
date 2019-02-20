package power.api.util.meterRecordCalculator;

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


    @Override
    public float countReactivePower(MeterRecord mr, Boolean three) {
        if (!three) {
            float apparentPowerTotal = this.countApparentPower(mr);
            return (float) (apparentPowerTotal * (-Math.cos(Math.PI / 2 + Math.toDegrees(Math.acos(mr.getActivePower() / apparentPowerTotal)))));
        } else {
            return 0;
        }
    }

    @Override
    public float countApparentPower(MeterRecord mr, Boolean three) {
        if (!three) {
            return (mr.getVa() + mr.getVb() + mr.getVc()) * (mr.getIa() + mr.getIb() + mr.getIc());
        } else {
            return 0;
        }
    }

    @Override
    public float countLineVoltage(double arg1, double arg2) {
        return (float) (Math.sqrt(Math.pow(arg1, 2) + Math.pow(arg2, 2) - 2 * arg1 * arg2) * Math.cos(Math.PI * 2 / 3));

    }


    /**
     * 计算Uab
     *
     * @param meterRecord
     * @return
     */
    public static float countLineVoltageUab(MeterRecord meterRecord) {
        return instance.countLineVoltage(meterRecord.getVa(), meterRecord.getVb());
    }

    /**
     * 计算Ubc
     *
     * @param meterRecord
     * @return
     */
    public static float countLineVoltageUbc(MeterRecord meterRecord) {
        return instance.countLineVoltage(meterRecord.getVb(), meterRecord.getVc());
    }

    /**
     * 计算Uca
     *
     * @param meterRecord
     * @return
     */
    public static float countLineVoltageUca(MeterRecord meterRecord) {
        return instance.countLineVoltage(meterRecord.getVc(), meterRecord.getVa());
    }


    /**
     * 非三相无功功率
     *
     * @param meterRecord
     * @return
     */
    public static float countReactivePower(MeterRecord meterRecord) {
        return instance.countReactivePower(meterRecord, false);
    }

    /**
     * 三相无功功率
     *
     * @param meterRecord
     * @return
     */
    public static float countReactivePowerThree(MeterRecord meterRecord) {
        return instance.countReactivePower(meterRecord, true);
    }

    /**
     * 非三相视在功率
     *
     * @param meterRecord
     * @return
     */
    public static float countApparentPower(MeterRecord meterRecord) {
        return instance.countReactivePower(meterRecord, false);
    }

    /**
     * 三相视在功率
     *
     * @param meterRecord
     * @return
     */
    public static float countApparentPowerThree(MeterRecord meterRecord) {
        return instance.countReactivePower(meterRecord, true);
    }
}

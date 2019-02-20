package power.api.util.meterRecordCalculator;

import power.api.model.MeterRecord;

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

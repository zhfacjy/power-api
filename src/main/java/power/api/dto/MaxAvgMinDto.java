package power.api.dto;

import lombok.Data;

/**
 * 这个类用来装载Sql查询后返回的结果
 */
@Data
public class MaxAvgMinDto {
    private double max;
    private double avg;
    private double min;
    private String days; //日期

    public MaxAvgMinDto() {
    }

    /**
     * 注意构造函数和../repository/MeterRecordRepository.java 中的findMaxAvgMinByPower方法
     * 位置是对应的
     *
     * @param max
     * @param avg
     * @param min
     * @param days
     */
    public MaxAvgMinDto(double max, double avg, double min, String days) {
        this.max = max;
        this.avg = avg;
        this.min = min;
        this.days = days;
    }
}

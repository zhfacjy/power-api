package power.api.dto;

/**
 * 装载相电压报表结果的数据结构
 */
public interface PhaseVoltageReportDto {
    Double getUa();

    Double getUb();

    Double getUc();

    Double getIa();

    Double getIb();

    Double getIc();

    Double getActivePower();

    String getCreateAt();

}

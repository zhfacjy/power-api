package power.api.dto;

public interface MeterRecordDto {
    Float getUa();

    Float getUb();

    Float getUc();

    Float getIa();

    Float getIb();

    Float getIc();

    Float getPfa();

    Float getPfb();

    Float getPfc();

    Double getActivePower();

    Integer getTemperature();

    Integer getCurrentLimit();

    Double getFrequency();

    Double getElectricEnergy();

    String getCreateAt();
}

package power.api.service.impl.DataWrapperClassHolder;

import lombok.Data;

@Data
public class LineVoltageHolder {
    public float Uab,Ubc,Uca;

    public LineVoltageHolder() {
    }
}
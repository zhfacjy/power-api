package power.api.service.impl.DataWrapperClassHolder;

import lombok.Data;

@Data
public class DegreeOfThreePhaseUnbalanceHolder {
    private float IUnb, UUnb;

    public DegreeOfThreePhaseUnbalanceHolder() {
    }
}